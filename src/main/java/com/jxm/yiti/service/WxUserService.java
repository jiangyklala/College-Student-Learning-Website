package com.jxm.yiti.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jxm.yiti.domain.WxUserInfo;
import com.jxm.yiti.mapper.WxUserInfoMapper;
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
                // 用户已注册
                // TODO
            } else {
                // 用户未注册, 进行注册步骤
                WxUserInfo newUser = new WxUserInfo();
                newUser.setOpenId(open_id);
                newUser.setPoints(100);  // 初始 100 积分
                wxUserInfoMapper.insertSelective(newUser);

                wxUserInfo = wxUserInfoMapperCust.selectAllByOpenId(open_id);
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
        try {
            Integer userPoints = wxUserInfoMapperCust.selectPointsById(wxUserId);
            if (userPoints < paymentReq.getPoints()) {
                commonResp.setCode(410);
                commonResp.setMessage("积分不足");
                return;
            }
            wxUserInfoMapperCust.payWithPoints(wxUserId, paymentReq.getPoints());
        } catch (RuntimeException e) {
            commonResp.setCode(400);
            commonResp.setMessage("服务异常");
        }
    }
}
