package com.jxm.yiti.req;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayReq {
    private String orderId;

    private BigDecimal amount;
}
