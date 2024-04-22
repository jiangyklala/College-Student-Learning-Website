package com.jxm.yiti.mapper.cust;

import org.apache.ibatis.annotations.Param;

public interface WxInviterMapperCust {
    Integer addUserEarnings(@Param("userId") Integer userId, @Param("increment") Integer increment);
}