package com.jxm.yiti.config;

/**
 * @author jiangyunkai <jiangyunkai@kuaishou.com>
 * Created on 2024-04-20
 */
public enum RedisKeyAliasName {
    // yiti:wxapp:config
    WXAPP_CONFIG_PREFIX("yt:wa:cf:"),
    WXAPP_INVITE_DISCOUNT("yt:wa:cf:id"),
    WXAPP_VIP_PRICE("yt:wa:cf:vp"),
    WXAPP_PAY_PAGE_SHOW("yt:wa:cf:pps"),
    ;

    private final String keyName;

    RedisKeyAliasName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyName() {
        return keyName;
    }

}
