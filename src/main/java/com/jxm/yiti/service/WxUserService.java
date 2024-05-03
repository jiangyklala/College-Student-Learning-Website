package com.jxm.yiti.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jxm.yiti.domain.*;
import com.jxm.yiti.enums.StatusCode;
import com.jxm.yiti.enums.WxUserConst;
import com.jxm.yiti.interceptor.WxAppInterceptor;
import com.jxm.yiti.mapper.*;
import com.jxm.yiti.mapper.cust.QuestionUserInfoMapperCust;
import com.jxm.yiti.mapper.cust.WxInviterMapperCust;
import com.jxm.yiti.mapper.cust.WxUserInfoMapperCust;
import com.jxm.yiti.req.AppPayInfoReq;
import com.jxm.yiti.req.PaymentReq;
import com.jxm.yiti.req.SearchLimitsReq;
import com.jxm.yiti.req.WxOnePaymentReq;
import com.jxm.yiti.resp.*;
import com.jxm.yiti.utils.CopyUtil;
import com.jxm.yiti.utils.InviteCodeGenerate;
import com.jxm.yiti.utils.SnowFlakeIdWorker;
import com.jxm.yiti.utils.TokenUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.*;

@Slf4j
@Service
public class WxUserService {

    @Value("${wxApp.appSecret}")
    private String appSecret;

    @Value("${wxApp.appId}")
    private String appId;

    @Value("${wxApp.login.secret}")
    private String loginSecret;

    @Resource
    QuestionUserInfoMapper questionUserInfoMapper;

    @Resource
    QuestionUserInfoMapperCust questionUserInfoMapperCust;

    @Resource
    private WxUserInfoMapperCust wxUserInfoMapperCust;

    @Resource
    private WxInviterMapperCust wxInviterMapperCust;

    @Resource
    private WxInviterMapper wxInviterMapper;

    @Resource
    private WxUserInfoMapper wxUserInfoMapper;

    @Resource
    private WxInviteeMapper wxInviteeMapper;

    @Resource
    WxInviteService wxInviteService;

    private final static Integer INIT_POINTS = 50;

    @Resource
    AppPayInfoMapper appPayInfoMapper;

    // 登录接口
    public void login(CommonResp2<WxLoginResp> commonResp, String code) throws IOException, URISyntaxException {
        WxLoginResp wxLoginResp = new WxLoginResp();
        String open_id = null;
        String session_key = null;

        // 获取 open_id 和 session_key
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URI uri = new URIBuilder("https://api.weixin.qq.com/sns/jscode2session")
                    .setParameter("appid", appId)
                    .setParameter("secret", appSecret)
                    .setParameter("js_code", code)
                    .setParameter("grant_type", "authorization_code")
                    .build();
            HttpGet httpGet = new HttpGet(uri);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    JSONObject responseJsonObj = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                    open_id = responseJsonObj.getString("openid");
                    session_key = responseJsonObj.getString("session_key");

                    log.info("session_key = " + session_key);
                }
            }
        }

        try {
            WxUserInfo wxUserInfo = wxUserInfoMapperCust.selectAllByOpenId(open_id);

            if (wxUserInfo != null) {
                // 用户已注册 do nothing
            } else {
                // 用户未注册, 进行注册步骤
                WxUserInfo newUser = new WxUserInfo();
                newUser.setOpenId(open_id);
                newUser.setPoints(INIT_POINTS);  // 初始积分
                newUser.setName(String.valueOf(new SnowFlakeIdWorker().nextId()));
                wxUserInfoMapper.insertSelective(newUser);

                wxUserInfo = wxUserInfoMapperCust.selectAllByOpenId(open_id);

                // 注册完之后, 进入其他模块的初始化工作
                initQuestionUserInfo(wxUserInfo.getId());
            }

            // 生成 auth_token
            wxLoginResp.setAuthToken(generateAuthToken(wxUserInfo.getId(), wxUserInfo.getType(), Duration.ofDays(360)));
            // 设置返回的用户信息
            wxLoginResp.setWxUserInfoResp(CopyUtil.copy(wxUserInfo, WxUserInfoResp.class));

        } catch (RuntimeException e) {
            commonResp.setCode(401);
            commonResp.setSuccess(false);
            commonResp.setMessage("用户登录失败!");
            log.error("用户登录失败", e);
            return;
        }

        commonResp.setContent(wxLoginResp);
        commonResp.setMessage("登录成功");
//        GenerateTokenUtil.decryptToken(wxLoginResp.getAuthToken(), loginSecret);
//        GenerateTokenUtil.checkIfExpired(wxLoginResp.getAuthToken(), loginSecret);
    }

    /**
     * 生成 auth_token
     */
    private String generateAuthToken(Integer userId, Integer userType, Duration expired) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", userId);
        jsonObject.put("user_type", userType);  // 用于直接在拦截器中拦截部分请求
        String encryptUserInfo = jsonObject.toString();

        return TokenUtil.wxAppAuthToken(encryptUserInfo, loginSecret, expired);
    }

    public void payForQuestion(CommonResp2 commonResp, PaymentReq paymentReq, Integer wxUserId) {
        boolean ifExistsInPayedSet = false;
        SortedSet<Integer> payedIdSet = null;
        QuestionUserInfo questionUserInfo = null;

        // 验证用户已购买 set 中是否含有 questionId
        try {
            questionUserInfo = questionUserInfoMapperCust.selectByUserId(wxUserId);
            payedIdSet = JSON.parseObject(questionUserInfo.getPayedIdSet(), SortedSet.class);
            ifExistsInPayedSet = payedIdSet.contains(paymentReq.getQuestionId());
        } catch (RuntimeException e) {
            log.error("userId = {}", wxUserId, e);
            commonResp.setCode(400);
            commonResp.setMessage("服务异常");
            return;
        }

        if (!ifExistsInPayedSet) {
            try {
                // 验证积分
                Integer userPoints = wxUserInfoMapperCust.selectPointsById(wxUserId);
                if (userPoints < paymentReq.getPoints()) {
                    commonResp.setCode(410);
                    commonResp.setMessage("积分不足");
                    return;
                }

                // 扣除积分
                wxUserInfoMapperCust.payWithPoints(wxUserId, paymentReq.getPoints());

                // 添加已购题目id
                payedIdSet.add(paymentReq.getQuestionId());
                questionUserInfo.setPayedIdSet(JSON.toJSONBytes(payedIdSet));
                questionUserInfoMapper.updateByPrimaryKeyWithBLOBs(questionUserInfo);
            } catch (RuntimeException e) {
                commonResp.setCode(400);
                commonResp.setMessage("服务异常");
            }
        }

    }

    public void refreshUserInfo(CommonResp2<WxUserInfoResp> commonResp, Integer wxUserId) {
        WxUserInfo wxUserInfo = wxUserInfoMapper.selectByPrimaryKey(wxUserId);
        WxUserInfoResp wxUserInfoResp = CopyUtil.copy(wxUserInfo, WxUserInfoResp.class);

        commonResp.setContent(wxUserInfoResp);

    }

    // 初始化用户题目信息记录模块
    public void initQuestionUserInfo(Integer userId) {
        SortedSet<Integer> payedIdSet = new TreeSet<>(Comparator.comparingInt(a -> a));   // 按 Integer 排序
        SortedSet<Integer> markedIdSet = new TreeSet<>(Comparator.comparingInt(a -> a));   // 按 Integer 排序
        QuestionUserInfo questionUserInfo = new QuestionUserInfo();

        questionUserInfo.setUserId(userId);
        questionUserInfo.setPayedIdSet(JSON.toJSONBytes(payedIdSet));
        questionUserInfo.setMarkedIdSet(JSON.toJSONBytes(markedIdSet));

        questionUserInfoMapper.insert(questionUserInfo);
    }

    public void payOneQuestion(CommonResp2 commonResp, WxOnePaymentReq wxOnePaymentReq, Integer wxUserId) {
        try {
            // 验证积分
            Integer userPoints = wxUserInfoMapperCust.selectPointsById(wxUserId);
            if (userPoints < wxOnePaymentReq.getPoints()) {
                commonResp.setCode(410);
                commonResp.setMessage("积分不足");
                return;
            }

            // 扣除积分
            wxUserInfoMapperCust.payWithPoints(wxUserId, wxOnePaymentReq.getPoints());
        } catch (RuntimeException e) {
            log.error("积分扣除失败", e);
            commonResp.setSuccess(false);
            commonResp.setCode(420);
            commonResp.setMessage("程序出错!");
        }
    }

    public ArrayList<String> getNVCDKeyWithNumAndTime(Integer option, Integer num, Integer time) {
        switch (option) {
            case 1 -> {
                return getCodeWithNumAndTime("yt:wa:cd:nv:", num, time);
            }
            case 2 -> {
                return getCodeWithNumAndTime("yt:wa:cd:sv:", num, time);
            }
        }

        return null;
    }

    public ArrayList<String> getCodeWithNumAndTime(String keyName, Integer num, Integer time) {
        ArrayList<String> codes = new ArrayList<>(num);
        try (Jedis jedis = UserService.jedisPool.getResource()) {
            for (int i = 0; i < num; ++i) {
                String code = InviteCodeGenerate.next();
                jedis.setex(keyName + code, time * 60, "");   // yiti:wxapp:code:normal_vip

                codes.add(code);
            }
        }
        return codes;
    }

    public void isCDKeyValid(CommonResp2<String> resp, String cdKey, Integer wxUserId) {
        try (Jedis jedis = UserService.jedisPool.getResource()) {
            log.info("yt:wa:cd:nv:" + cdKey);
            if (jedis.exists("yt:wa:cd:nv:" + cdKey)) {
                jedis.del("yt:wa:cd:nv:" + cdKey);

                // 切换用户类型后, 需要重新签发 token
                wxUserInfoMapperCust.switchUserTypeByCDKey(wxUserId, WxUserConst.NORMAL_VIP.getType());
                resp.setContent(generateAuthToken(wxUserId, WxUserConst.NORMAL_VIP.getType(), Duration.ofDays(360)));
                return;
            }

            if (jedis.exists("yt:wa:cd:sv:" + cdKey)) {
                jedis.del("yt:wa:cd:sv:" + cdKey);
                wxUserInfoMapperCust.switchUserTypeByCDKey(wxUserId, WxUserConst.SPECIAL_VIP.getType());
                resp.setContent(generateAuthToken(wxUserId, WxUserConst.SPECIAL_VIP.getType(), Duration.ofDays(360)));
                return;
            }
        } catch (RuntimeException e) {
            resp.setCode(420);
            resp.setMessage("兑换失败!");
            return;
        }

        resp.setSuccess(false);
        resp.setMessage("兑换码不存在!");
    }

    public String switchUserType(String name, Integer option) {
        if (option >= 0 && option <= 3) {
            try {
                wxUserInfoMapperCust.switchUserTypeByName(name, option);
            } catch (RuntimeException e) {
                return "数据库错误!";
            }
        } else {
            return "option 参数错误";
        }

        return "设置成功";
    }

    public void payAsyncNotifyAfter(AppPayInfoReq notifyData) {
        log.debug("notifyData: {}", JSON.toJSONString(notifyData));
        AppPayInfo appPayInfo = CopyUtil.copy(notifyData, AppPayInfo.class);
        AppPayInfoExample appPayInfoExample = new AppPayInfoExample();
        AppPayInfoExample.Criteria criteria = appPayInfoExample.createCriteria();
        criteria.andOutTradeNoEqualTo(notifyData.getOutTradeNo());

        try {
            List<AppPayInfo> appPayInfos = appPayInfoMapper.selectByExample(appPayInfoExample);
            if (appPayInfos.isEmpty()) {
                throw new RuntimeException("selected appPayInfos is empty, trade_no is: " + notifyData.getOutTradeNo());
            }

            appPayInfo.setId(appPayInfos.get(0).getId());
            appPayInfo.setResultCode(notifyData.getResultCode());
            appPayInfo.setAppId(notifyData.getAppId());
            appPayInfo.setOpenId(notifyData.getOpenId());
            appPayInfo.setTransactionId(notifyData.getTransactionId());
            log.debug("appPayInfo: {}", JSON.toJSONString(appPayInfo));

            appPayInfoMapper.updateByPrimaryKeySelective(appPayInfo);
            wxUserInfoMapperCust.switchUserTypeByCDKey(appPayInfos.get(0).getUserId(), WxUserConst.NORMAL_VIP.getType());

            // 邀请逻辑
            solveInvite(appPayInfos.get(0));

        } catch (RuntimeException e) {
            log.error("wxApp pay notify error. appPayInfo:{}", JSON.toJSONString(appPayInfo), e);
        }
    }

    private void solveInvite(AppPayInfo appPayInfo) throws RuntimeException {
        if (appPayInfo.getInviterId() == null) {
            return;
        }

        // 增加收益
        WxInviter wxInviter = wxInviterMapper.selectByPrimaryKey(appPayInfo.getInviterId());
        BigDecimal earnRate = new BigDecimal(wxInviter.getEarnRate()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        BigDecimal count = new BigDecimal(appPayInfo.getTotalFee()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        wxInviterMapperCust.addUserEarnings(wxInviter.getInviterId(), count.multiply(earnRate));
        wxInviterMapperCust.incrInvitedCount(wxInviter.getInviterId());

        WxInvitee wxInvitee = new WxInvitee();
        wxInvitee.setInviteeId(appPayInfo.getUserId());
        wxInvitee.setInviterId(wxInviter.getInviterId());
        wxInvitee.setCount(String.valueOf(count));
        wxInvitee.setCreateTime(new Date());

        wxInviteeMapper.insertSelective(wxInvitee);
    }

    public void makeOrder(AppPayInfoReq req, Integer wxUserId, CommonResp2<String> resp) {
        // 判断是否已经是会员
        if (WxAppInterceptor.getWxUserType() != WxUserConst.NORMAL_USER) {
            log.info("userId: {}, userType: {}", wxUserId, WxAppInterceptor.getWxUserType());
            resp.setSuccess(false);
            resp.setMessage("已经是会员了喔~");
            return;
        }

        log.info("makeOrder Req: {}", JSON.toJSONString(req));
        AppPayInfo appPayInfo = CopyUtil.copy(req, AppPayInfo.class);
        appPayInfo.setUserId(wxUserId);
        log.info("appPayInfo: {}", JSON.toJSONString(appPayInfo));

        try {
            appPayInfoMapper.insertSelective(appPayInfo);
        } catch (Exception e) {
            log.error("wxApp make order error. appPayInfo:{}", JSON.toJSONString(appPayInfo), e);
        }
    }

    public void refreshAuthToken(Integer wxUserId, CommonResp2<String> resp) {
        try {
            Integer userType = wxUserInfoMapperCust.selectTypeByUserId(wxUserId);
            String authToken = generateAuthToken(wxUserId, userType, Duration.ofDays(360));
            resp.setContent(authToken);
            log.debug("userId:{} refreshed token: {}", wxUserId, authToken);
        } catch (RuntimeException e) {
            resp.setCode(420);
            resp.setMessage("refresh token failed!");
        }
    }

    public void searchLimits(SearchLimitsReq req, CommonResp2<WxInviterLimitsResp> resp) {
        try {
            WxUserInfoExample wxUserInfoExample = new WxUserInfoExample();
            WxUserInfoExample.Criteria criteria = wxUserInfoExample.createCriteria();
            criteria.andNameEqualTo(req.getUserName());

            List<WxUserInfo> wxUserInfos = wxUserInfoMapper.selectByExample(wxUserInfoExample);
            if (wxUserInfos == null || wxUserInfos.isEmpty()) {
                resp.setSuccess(false);
                resp.setCode(StatusCode.BUSINESS_BUSY.code);
                resp.setMessage("查找不到相应用户");
                return;
            }

            Integer inviteUserId = wxUserInfos.get(0).getId();
            WxInviter wxInviter = wxInviterMapper.selectByPrimaryKey(inviteUserId);
            if (wxInviter == null) {
                wxInviter = wxInviteService.addInviter(inviteUserId, resp);
            }

            resp.setContent(CopyUtil.copy(wxInviter, WxInviterLimitsResp.class));
        } catch (RuntimeException e) {
            resp.setCode(StatusCode.DB_ERROR.code);
            resp.setMessage("refresh token failed!");
        }
    }

    public void searchLimitsSubmit(WxInviterLimitsReq req, CommonResp2<WxInviterLimitsResp> resp) {
        try {
            WxInviter wxInviter = CopyUtil.copy(req, WxInviter.class);
            wxInviterMapper.updateByPrimaryKeySelective(wxInviter);
        } catch (RuntimeException e) {
            resp.setCode(StatusCode.DB_ERROR.code);
            resp.setMessage("refresh token failed!");
        }
    }
}
