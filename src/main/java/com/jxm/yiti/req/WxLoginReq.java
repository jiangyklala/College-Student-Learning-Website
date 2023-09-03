package com.jxm.yiti.req;

import lombok.Data;

@Data
public class WxLoginReq {

    // wx.login 返回的临时 code
    private String code;
}
