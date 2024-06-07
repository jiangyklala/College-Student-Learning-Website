package com.jxm.yiti.resp;

import com.jxm.yiti.enums.StatusCode;
import lombok.Getter;
import lombok.Setter;

import static com.jxm.yiti.enums.StatusCode.SUCCESS;
import static com.jxm.yiti.enums.StatusCode.SYSTEM_BUSY;

@Setter
@Getter
public class CommonResp2<T> extends CommonResp<T> {

    private Integer code = SUCCESS.code;

    private final static String SYSTEM_ERROR_MSG = "系统错误";

    public CommonResp2<T> buildSuccess(T content) {
        CommonResp2<T> resp = new CommonResp2<>();
        resp.setContent(content);
        return resp;
    }

    public CommonResp2<T> buildFailure(StatusCode code, String message, T content) {
        CommonResp2<T> resp = new CommonResp2<>();
        resp.setSuccess(false);
        resp.setContent(content);
        resp.setCode(code.getCode());
        resp.setMessage(message);
        return resp;
    }

    public CommonResp2<T> buildFailure(StatusCode code, String message) {
        CommonResp2<T> resp = new CommonResp2<>();
        resp.setSuccess(false);
        resp.setCode(code.getCode());
        resp.setMessage(message);
        return resp;
    }

    public CommonResp2<T> buildFailure(StatusCode code) {
        CommonResp2<T> resp = new CommonResp2<>();
        resp.setSuccess(false);
        resp.setCode(code.getCode());
        resp.setMessage(SYSTEM_ERROR_MSG);
        return resp;
    }

    public CommonResp2<T> buildFailure(String message, T content) {
        CommonResp2<T> resp = new CommonResp2<>();
        resp.setSuccess(false);
        resp.setCode(SYSTEM_BUSY.getCode());
        resp.setContent(content);
        resp.setMessage(message);
        return resp;
    }

    public CommonResp2<T> buildFailure(String message) {
        CommonResp2<T> resp = new CommonResp2<>();
        resp.setSuccess(false);
        resp.setCode(SYSTEM_BUSY.getCode());
        resp.setMessage(message);
        return resp;
    }

    public void setFailure(String message) {
        this.setSuccess(false);
        this.setCode(SYSTEM_BUSY.getCode());
        this.setMessage(message);
    }
}
