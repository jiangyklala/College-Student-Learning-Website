package com.jxm.yiti.mapper.cust;

import org.apache.ibatis.annotations.Param;

import com.jxm.yiti.domain.WxUserInfo;

public interface WxUserInfoMapperCust {
    WxUserInfo selectAllByOpenId(@Param("openId") String openId);

    Integer selectTypeByUserId(@Param("userId") Integer userId);

    Integer selectPointsById(@Param("userId") Integer userId);

    Integer payWithPoints(@Param("userId") Integer userId, @Param("points") Integer points);

    Integer switchUserTypeByCDKey(@Param("userId") Integer userId, @Param("type") Integer type);

    Integer switchUserTypeByName(@Param("name") String name, @Param("type") Integer type);
}