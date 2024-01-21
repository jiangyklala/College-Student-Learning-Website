package com.jxm.yiti.domain;

public class WxCollect {
    private Integer id;

    private Integer userId;

    private byte[] collectIdSet;

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

    public byte[] getCollectIdSet() {
        return collectIdSet;
    }

    public void setCollectIdSet(byte[] collectIdSet) {
        this.collectIdSet = collectIdSet;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", collectIdSet=").append(collectIdSet);
        sb.append("]");
        return sb.toString();
    }
}