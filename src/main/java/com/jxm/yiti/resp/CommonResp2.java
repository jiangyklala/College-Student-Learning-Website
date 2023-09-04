package com.jxm.yiti.resp;

public class CommonResp2<T> extends CommonResp<T> {

    private Integer code = 200;

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
