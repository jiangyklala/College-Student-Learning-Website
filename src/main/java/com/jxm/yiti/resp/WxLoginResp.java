package com.jxm.yiti.resp;

import com.jxm.yiti.domain.WxUserInfo;
import lombok.Data;

@Data
public class WxLoginResp {

    private WxUserInfo wxUserInfo;

    private String userToken;
}
