package com.jxm.yiti.mapper.cust;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface WxInviterMapperCust {
    Integer addUserEarnings(@Param("userId") Integer userId, @Param("increment") BigDecimal increment);
}