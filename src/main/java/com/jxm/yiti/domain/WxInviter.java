package com.jxm.yiti.domain;

import java.math.BigDecimal;

public class WxInviter {
    private Integer inviterId;

    private Integer invitedCount;

    private BigDecimal inviteBalance;

    private Integer earnRate;

    private BigDecimal earnings;

    private Boolean isAccessible;

    public Integer getInviterId() {
        return inviterId;
    }

    public void setInviterId(Integer inviterId) {
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

    public Boolean getIsAccessible() {
        return isAccessible;
    }

    public void setIsAccessible(Boolean isAccessible) {
        this.isAccessible = isAccessible;
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
        sb.append(", isAccessible=").append(isAccessible);
        sb.append("]");
        return sb.toString();
    }
}