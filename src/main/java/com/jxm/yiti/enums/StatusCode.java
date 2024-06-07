package com.jxm.yiti.enums;

import lombok.Getter;

@Getter
public enum StatusCode {
    SUCCESS(200),
    DB_ERROR(420),
    BUSINESS_BUSY(499),
    SYSTEM_BUSY(500);
    ;

    public final Integer code;

    StatusCode(int code) {
        this.code = code;
    }
}
