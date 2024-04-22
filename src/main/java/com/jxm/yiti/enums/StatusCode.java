package com.jxm.yiti.enums;

public enum StatusCode {
    SUCCESS(200),
    DB_ERROR(420),
    BUSINESS_BUSY(499),
    ;

    public final Integer code;

    StatusCode(int code) {
        this.code = code;
    }
}
