package com.jxm.yiti.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.hc.core5.net.URIBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jxm.yiti.domain.AppPayInfo;
import com.jxm.yiti.domain.AppPayInfoExample;
import com.jxm.yiti.domain.QuestionUserInfo;
import com.jxm.yiti.domain.WxUserInfo;
import com.jxm.yiti.enums.WxUserConst;
import com.jxm.yiti.mapper.AppPayInfoMapper;
import com.jxm.yiti.mapper.QuestionUserInfoMapper;
import com.jxm.yiti.mapper.WxUserInfoMapper;
import com.jxm.yiti.mapper.cust.QuestionUserInfoMapperCust;
import com.jxm.yiti.mapper.cust.WxUserInfoMapperCust;
import com.jxm.yiti.req.AppPayInfoReq;
import com.jxm.yiti.req.PaymentReq;
import com.jxm.yiti.req.WxOnePaymentReq;
import com.jxm.yiti.resp.CommonResp2;
import com.jxm.yiti.resp.WxLoginResp;
import com.jxm.yiti.resp.WxUserInfoResp;
import com.jxm.yiti.utils.CopyUtil;
import com.jxm.yiti.utils.InviteCodeGenerate;
import com.jxm.yiti.utils.SnowFlakeIdWorker;
import com.jxm.yiti.utils.TokenUtil;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

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
    private WxUserInfoMapper wxUserInfoMapper;

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
                newUser.setPoints(500);  // 初始 100 积分
                newUser.setName(String.valueOf(new SnowFlakeIdWorker().nextId()));
                wxUserInfoMapper.insertSelective(newUser);

                wxUserInfo = wxUserInfoMapperCust.selectAllByOpenId(open_id);

                // 注册完之后, 进入其他模块的初始化工作
                initQuestionUserInfo(wxUserInfo.getId());
            }

            // 生成 auth_token
            wxLoginResp.setAuthToken(generateAuthToken(wxUserInfo.getId(), wxUserInfo.getType(), Duration.ofDays(30)));
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
                resp.setContent(generateAuthToken(wxUserId, WxUserConst.NORMAL_VIP.getType(), Duration.ofDays(30)));
                return;
            }

            if (jedis.exists("yt:wa:cd:sv:" + cdKey)) {
                jedis.del("yt:wa:cd:sv:" + cdKey);
                wxUserInfoMapperCust.switchUserTypeByCDKey(wxUserId, WxUserConst.SPECIAL_VIP.getType());
                resp.setContent(generateAuthToken(wxUserId, WxUserConst.SPECIAL_VIP.getType(), Duration.ofDays(30)));
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
        log.debug("notifyData: {}", notifyData.toString());
        AppPayInfo appPayInfo = CopyUtil.copy(notifyData, AppPayInfo.class);
        AppPayInfoExample appPayInfoExample = new AppPayInfoExample();
        AppPayInfoExample.Criteria criteria = appPayInfoExample.createCriteria();
        criteria.andOutTradeNoEqualTo(notifyData.getOutTradeNo());

        try {
            List<AppPayInfo> appPayInfos = appPayInfoMapper.selectByExample(appPayInfoExample);
            if (appPayInfos.isEmpty()) {
                throw new Exception("appPayInfos is empty");
            }

            appPayInfo.setId(appPayInfos.get(0).getId());
            appPayInfo.setResultCode(notifyData.getResultCode());
            appPayInfo.setAppId(notifyData.getAppId());
            appPayInfo.setOpenId(notifyData.getOpenId());
            appPayInfo.setTransactionId(notifyData.getTransactionId());
            log.debug("appPayInfo: {}", appPayInfo.toString());

            appPayInfoMapper.updateByPrimaryKeySelective(appPayInfo);
            wxUserInfoMapperCust.switchUserTypeByCDKey(appPayInfos.get(0).getUserId(), WxUserConst.NORMAL_VIP.getType());
        } catch (Exception e) {
            log.error("wxApp pay notify error. appPayInfo:{}", appPayInfo, e);
        }
    }

    public void makeOrder(AppPayInfoReq req, Integer wxUserId) {
        log.debug("makeOrder Req: {}", req.toString());
        AppPayInfo appPayInfo = CopyUtil.copy(req, AppPayInfo.class);
        appPayInfo.setUserId(wxUserId);
        log.debug("appPayInfo: {}", appPayInfo.toString());

        try {
            appPayInfoMapper.insertSelective(appPayInfo);
        } catch (Exception e) {
            log.error("wxApp make order error. appPayInfo:{}", appPayInfo, e);
        }
    }
}
