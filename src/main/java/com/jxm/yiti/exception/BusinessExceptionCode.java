package com.jxm.yiti.exception;

public enum BusinessExceptionCode {
    USER_LOGIN_NAME_EXIST("登录名已存在"),
    SYSTEM_ERROR("系统出错"),
    ;

    private String desc;


    BusinessExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
