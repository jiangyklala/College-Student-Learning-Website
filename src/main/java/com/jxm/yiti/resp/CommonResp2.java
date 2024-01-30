package com.jxm.yiti.resp;

import static com.jxm.yiti.enums.StatusCode.SUCCESS;

public class CommonResp2<T> extends CommonResp<T> {

    private Integer code = SUCCESS.code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "CommonResp2{" +
                "code=" + code +
                "} " + super.toString();
    }
}
