package com.jxm.yiti.mapper.cust;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

public interface WxInviterMapperCust {
    Integer addUserEarnings(@Param("userId") Integer userId, @Param("increment") BigDecimal increment);
}