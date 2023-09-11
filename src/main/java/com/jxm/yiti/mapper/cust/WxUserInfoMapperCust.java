package com.jxm.yiti.mapper.cust;

import com.jxm.yiti.domain.WxUserInfo;
import org.apache.ibatis.annotations.Param;

public interface WxUserInfoMapperCust {
    WxUserInfo selectAllByOpenId(@Param("openId") String openId);

    Integer selectPointsById(@Param("userId") Integer userId);

    Integer payWithPoints(@Param("userId") Integer userId, @Param("points") Integer points);
}