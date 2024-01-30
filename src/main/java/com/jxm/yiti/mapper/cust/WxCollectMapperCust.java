package com.jxm.yiti.mapper.cust;

import org.apache.ibatis.annotations.Param;

import com.jxm.yiti.domain.WxCollect;
import com.jxm.yiti.domain.cust.WxCollectBLOB;

public interface WxCollectMapperCust {

    WxCollectBLOB selectByUserIdWithBLOBsSingle(@Param("userId") Integer userId);

    WxCollect selectByUserIdWithBLOBs(@Param("userId") Integer userId);

    int updateByUserIdWithBLOBs(@Param("record") WxCollect record);
}