package com.jxm.yiti.resp;

import java.math.BigDecimal;
import java.util.List;

public class GptInviterResp {
    private Integer invitedCount;

    private BigDecimal inviteBalance;

    private Integer earnRate;

    private BigDecimal earnings;

    public Integer getInvitedCount() {
        return invitedCount;
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

    public void setInvitedCount(Integer invitedCount) {
        this.invitedCount = invitedCount;
    }

    public BigDecimal getEarnings() {
        return earnings;
    }

    public void setEarnings(BigDecimal earnings) {
        this.earnings = earnings;
    }

    @Override
    public String toString() {
        return "GptInviterResp{" +
                "invitedCount=" + invitedCount +
                ", inviteBalance=" + inviteBalance +
                ", earnRate=" + earnRate +
                ", earnings=" + earnings +
                '}';
    }
}