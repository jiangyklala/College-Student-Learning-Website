package com.jxm.yiti.domain;

public class QuestionUserInfo {
    private Integer id;

    private Integer userId;

    private byte[] payedIdSet;

    private byte[] markedIdSet;

    private byte[] other;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public byte[] getPayedIdSet() {
        return payedIdSet;
    }

    public void setPayedIdSet(byte[] payedIdSet) {
        this.payedIdSet = payedIdSet;
    }

    public byte[] getMarkedIdSet() {
        return markedIdSet;
    }

    public void setMarkedIdSet(byte[] markedIdSet) {
        this.markedIdSet = markedIdSet;
    }

    public byte[] getOther() {
        return other;
    }

    public void setOther(byte[] other) {
        this.other = other;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", payedIdSet=").append(payedIdSet);
        sb.append(", markedIdSet=").append(markedIdSet);
        sb.append(", other=").append(other);
        sb.append("]");
        return sb.toString();
    }
}