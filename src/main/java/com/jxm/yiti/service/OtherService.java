package com.jxm.yiti.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jxm.yiti.enums.StatusCode;
import com.jxm.yiti.req.ConfigListReq;
import com.jxm.yiti.req.SetConfigReq;
import com.jxm.yiti.resp.CommonResp2;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @author jiangyunkai <jiangyunkai@kuaishou.com>
 * Created on 2024-04-18
 */
@Slf4j
@Service
public class OtherService {

    public static String selectConfig(String configName, CommonResp2 resp) {
        try (Jedis jedis = UserService.jedisPool.getResource()) {
            return jedis.get(configName);
        } catch (RuntimeException e) {
            resp.setSuccess(false);
            resp.setCode(StatusCode.DB_ERROR.code);
            return null;
        }
    }

    public void setConfig(SetConfigReq req, CommonResp2 resp) {
        try (Jedis jedis = UserService.jedisPool.getResource()) {
            log.debug("req: {}", JSON.toJSONString(req));
            jedis.set(req.getName(), req.getContent());
        } catch (RuntimeException e) {
            resp.setSuccess(false);
            resp.setCode(StatusCode.DB_ERROR.code);
        }
    }

    public void selectAllConfig(ConfigListReq req, CommonResp2<String> resp) {
        List<String> newConfigList = req.getName().stream()
                .map(s -> req.getPrefix() + s)
                .toList();
        log.debug("req: {}", JSON.toJSONString(newConfigList));
        List<String> mGetRes = new ArrayList<>();
        try (Jedis jedis = UserService.jedisPool.getResource()) {
            mGetRes = jedis.mget(newConfigList.toArray(new String[0]));
        } catch (RuntimeException e) {
            resp.setSuccess(false);
            resp.setCode(StatusCode.DB_ERROR.code);
        }

        if (mGetRes.size() != newConfigList.size()) {
            resp.setSuccess(false);
            resp.setContent("Error!");
        }

        JSONObject resJSON = new JSONObject();
        for (int i = 0; i < newConfigList.size(); ++i) {
            resJSON.put(newConfigList.get(i).replace(":", "_"), mGetRes.get(i));
        }

        log.debug("resp: {}", JSON.toJSONString(resJSON));
        resp.setContent(resJSON.toJSONString());
    }

    public void setConfigList(List<String> req, CommonResp2 resp) {
        if (req == null || req.isEmpty()) {
            resp.setSuccess(false);
            resp.setCode(StatusCode.BUSINESS_BUSY.code);
            resp.setMessage("请求为空!");
            return;
        }

        log.debug("req: {}", JSON.toJSONString(req));
        try (Jedis jedis = UserService.jedisPool.getResource()) {
            jedis.mset(req.toArray(new String[0]));
        } catch (RuntimeException e) {
            resp.setSuccess(false);
            resp.setCode(StatusCode.DB_ERROR.code);
        }
    }
}
