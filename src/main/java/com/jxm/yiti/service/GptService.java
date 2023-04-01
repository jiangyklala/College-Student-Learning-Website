package com.jxm.yiti.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Service
public class GptService {

    public String sendPost(String data) {
        String res = "";
        String proxyHost = "127.0.0.1";
        int proxyPort = 7890;

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        factory.setProxy(proxy);
        RestTemplate client = new RestTemplate(factory);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer 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");
        httpHeaders.add("Content-Type", "application/json"); // 传递请求体时必须设置

        String requestJson = String.format(
                "{\n" +
                        "    \"model\": \"gpt-3.5-turbo-0301\",\n" +
                        "    \"messages\":" +
                            "[{\"role\": \"user\", \"content\": \"%s\"}],\n" +
                        "    \"temperature\": 0, \n" +
                        "    \"max_tokens\": 2048\n" +
                        "}",data
        );
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,httpHeaders);
        ResponseEntity<String> response = client.exchange("https://api.openai.com/v1/chat/completions", HttpMethod.POST, entity, String.class);
        System.out.println(response.getBody());
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        if (jsonObject != null) {
            JSONArray choices = jsonObject.getJSONArray("choices");
            res = choices.getJSONObject(0).getJSONObject("message").getString("content");

        }
        return res;
    }

}
