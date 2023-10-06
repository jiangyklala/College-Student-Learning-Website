package com.jxm.yiti.enums;

import java.util.Arrays;

public enum WxUserConst {
    UNKNOWN(-1),
    NORMAL_USER(0),
    NORMAL_VIP(1),
    SPECIAL_VIP(2),  // 训练营会员
    SUPER(3),
    ;

    public final int type;

    private WxUserConst(int value) {
        this.type = value;
    }

    public int getType() {
        return type;
    }

    public static WxUserConst of(int valueType) {
        return Arrays.stream(values())
                .filter(e -> e.type == valueType)
                .findFirst()
                .orElse(UNKNOWN);
    }
}
