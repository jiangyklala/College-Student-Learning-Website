package com.jxm.yiti.resp;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class WxInviterLimitsResp {

    private Integer inviterId;

    private Integer earnRate;

    private Boolean isAccessible;

    private BigDecimal earnings;

    private BigDecimal inviteBalance;
}