package com.jxm.yiti.resp;

import java.util.List;

public class GptInviterResp {
    private Integer invitedCount;

    private Integer inviteBalance;

    private Integer earnRate;

    private Integer earnings;

    public Integer getInvitedCount() {
        return invitedCount;
    }

    public Integer getInviteBalance() {
        return inviteBalance;
    }

    public void setInviteBalance(Integer inviteBalance) {
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

    public Integer getEarnings() {
        return earnings;
    }

    public void setEarnings(Integer earnings) {
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