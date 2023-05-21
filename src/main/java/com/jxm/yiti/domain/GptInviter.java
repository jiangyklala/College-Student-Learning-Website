package com.jxm.yiti.domain;

import java.math.BigDecimal;

public class GptInviter {
    private Long inviterId;

    private Integer invitedCount;

    private BigDecimal inviteBalance;

    private Integer earnRate;

    private BigDecimal earnings;

    public Long getInviterId() {
        return inviterId;
    }

    public void setInviterId(Long inviterId) {
        this.inviterId = inviterId;
    }

    public Integer getInvitedCount() {
        return invitedCount;
    }

    public void setInvitedCount(Integer invitedCount) {
        this.invitedCount = invitedCount;
    }

    public BigDecimal getInviteBalance() {
        return inviteBalance;
    }

    public void setInviteBalance(BigDecimal inviteBalance) {
        this.inviteBalance = inviteBalance;
    }

    public Integer getEarnRate() {
        return earnRate;
    }

    public void setEarnRate(Integer earnRate) {
        this.earnRate = earnRate;
    }

    public BigDecimal getEarnings() {
        return earnings;
    }

    public void setEarnings(BigDecimal earnings) {
        this.earnings = earnings;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", inviterId=").append(inviterId);
        sb.append(", invitedCount=").append(invitedCount);
        sb.append(", inviteBalance=").append(inviteBalance);
        sb.append(", earnRate=").append(earnRate);
        sb.append(", earnings=").append(earnings);
        sb.append("]");
        return sb.toString();
    }
}