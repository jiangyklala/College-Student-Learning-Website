package com.jxm.yiti.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jxm.yiti.domain.WxUserInfo;
import com.jxm.yiti.mapper.WxUserInfoMapper;
import com.jxm.yiti.mapper.cust.WxUserInfoMapperCust;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.WxLoginResp;
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

@Slf4j
@Service
public class WxUserService {

    @Value("${wxApp.appSecret}")
    private String appSecret;

    @Value("${wxApp.appId}")
    private String appId;

    @Resource
    private WxUserInfoMapperCust wxUserInfoMapperCust;

    @Resource
    private WxUserInfoMapper wxUserInfoMapper;

    // 登录接口
    public void login(CommonResp<WxLoginResp> commonResp, String code) throws IOException, URISyntaxException {
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
                wxLoginResp.setUserToken(open_id);
                wxLoginResp.setWxUserInfo(wxUserInfo);
            } else {
                // 用户未注册
                WxUserInfo newUser = new WxUserInfo();
                newUser.setOpenId(open_id);
                newUser.setPoints(0);
                wxUserInfoMapper.insertSelective(newUser);

                wxLoginResp.setUserToken(open_id);
                wxLoginResp.setWxUserInfo(wxUserInfoMapperCust.selectAllByOpenId(open_id));
            }
        } catch (RuntimeException e) {
            commonResp.setSuccess(false);
            commonResp.setMessage("用户登录失败!");
            log.error("用户登录失败", e);
            return;
        }

        commonResp.setContent(wxLoginResp);
        commonResp.setMessage("登录成功");
    }
}
