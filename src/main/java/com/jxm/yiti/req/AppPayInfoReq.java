package com.jxm.yiti.req;

import lombok.Data;

@Data
public class AppPayInfoReq {

    private String resultCode;

    private String transactionId;

    private String appId;

    private String openId;

    private Integer totalFee;

    private String outTradeNo;

    private String subMchId;

    private Integer inviterId;
}