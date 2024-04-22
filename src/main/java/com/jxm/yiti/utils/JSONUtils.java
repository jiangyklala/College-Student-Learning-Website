package com.jxm.yiti.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson2.JSON;
import com.jxm.yiti.resp.CommonResp2;
import com.jxm.yiti.service.OtherService;

/**
 * @author jiangyunkai <jiangyunkai@kuaishou.com>
 * Created on 2024-04-21
 */
public class JSONUtils {

    public static String addConfig(CommonResp2 resp, String... configNames) {
        Map<String, String> map = new ConcurrentHashMap<>(configNames.length * 2);

        Arrays.stream(configNames)
                .parallel()
                .forEach(configName -> {
                    String config = OtherService.selectConfig(configName, resp);
                    if (config == null) {
                        return;
                    }

                    map.put(configName.replaceAll(":", "_"), config);
                });

        return JSON.toJSONString(map);
    }
}
