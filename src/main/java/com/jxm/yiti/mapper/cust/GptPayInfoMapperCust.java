package com.jxm.yiti.mapper.cust;

import com.jxm.yiti.domain.GptPayInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GptPayInfoMapperCust {

    /**
     * 根据 order_no (订单号) 查某个订单信息
     *
     * @param orderNo 订单号
     * @return 某个订单信息
     */
    GptPayInfo selectByOrderNo(@Param("order_no") Long orderNo);

    List<GptPayInfo> selectPayHistory(@Param("from") String from, @Param("to") String to);
}