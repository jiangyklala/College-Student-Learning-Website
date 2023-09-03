package com.jxm.yiti.utils;

import redis.clients.jedis.Jedis;

public class JedisUtil {

    // get 若不存在返回 "0"
    public static String getOrZero(Jedis jedis, String command) {
        String res = jedis.get(command);
        if (res == null) {
            return "0";
        }
        return res;
    }
}
