package com.jxm.yiti.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jxm.yiti.domain.QuestionUserInfo;
import com.jxm.yiti.domain.WxUserInfo;
import com.jxm.yiti.mapper.QuestionUserInfoMapper;
import com.jxm.yiti.mapper.WxUserInfoMapper;
import com.jxm.yiti.mapper.cust.QuestionUserInfoMapperCust;
import com.jxm.yiti.mapper.cust.WxUserInfoMapperCust;
import com.jxm.yiti.req.PaymentReq;
import com.jxm.yiti.resp.CommonResp2;
import com.jxm.yiti.resp.WxLoginResp;
import com.jxm.yiti.resp.WxUserInfoResp;
import com.jxm.yiti.utils.CopyUtil;
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

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
                newUser.setPoints(100);  // 初始 100 积分
                wxUserInfoMapper.insertSelective(newUser);

                wxUserInfo = wxUserInfoMapperCust.selectAllByOpenId(open_id);

                // 注册完之后, 进入其他模块的初始化工作
                initQuestionUserInfo(wxUserInfo.getId());
            }

            // 设置 auth_token 加密信息
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", wxUserInfo.getId());
            String encryptUserInfo = jsonObject.toString();
            // 生成 auth_token
            wxLoginResp.setAuthToken(TokenUtil.wxAppAuthToken(encryptUserInfo, loginSecret, Duration.ofHours(1)));
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
}
