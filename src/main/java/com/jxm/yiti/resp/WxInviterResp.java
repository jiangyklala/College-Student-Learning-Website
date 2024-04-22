package com.jxm.yiti.resp;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class WxInviterResp {

    private Integer invitedCount;

    private BigDecimal inviteBalance;

    private Integer earnRate;

    private BigDecimal earnings;

    private Boolean isAccessible;
}