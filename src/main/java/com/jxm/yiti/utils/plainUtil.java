package com.jxm.yiti.utils;

import java.time.LocalTime;

public class plainUtil {

    /**
     * 获取当天剩余的秒数
     */
    public static Long getTodayRemainSec() {
        // 获取当前时间
        LocalTime now = LocalTime.now();

        // 获取当天的最后一秒
        LocalTime endOfDay = LocalTime.MAX;

        // 计算当前时间到当天最后一秒之间的差值，并获取剩余的秒数
        return (long) (endOfDay.toSecondOfDay() - now.toSecondOfDay());
    }

}
