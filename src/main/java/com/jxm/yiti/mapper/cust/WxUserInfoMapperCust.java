package com.jxm.yiti.mapper.cust;

import com.jxm.yiti.domain.WxUserInfo;

public interface WxUserInfoMapperCust {
    WxUserInfo selectAllByOpenId(String openId);
}