package com.jxm.yiti.resp;

import lombok.Data;

@Data
public class WxLoginResp {

    private WxUserInfoResp wxUserInfoResp;

    private String authToken;
}
