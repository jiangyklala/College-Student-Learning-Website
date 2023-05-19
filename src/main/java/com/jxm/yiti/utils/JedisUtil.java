package com.jxm.yiti.utils;

import redis.clients.jedis.Jedis;

public class JedisUtil {
    public static String getIfNotToZero(Jedis jedis, String command) {
        String res = jedis.get(command);
        if (res == null) {
            return "0";
        }
        return res;
    }
}
