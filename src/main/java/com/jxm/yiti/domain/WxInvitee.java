package com.jxm.yiti.domain;

import java.util.Date;

public class WxInvitee {
    private Integer id;

    private Integer inviteeId;

    private Integer inviterId;

    private String inviterName;

    private Integer kind;

    private String count;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInviteeId() {
        return inviteeId;
    }

    public void setInviteeId(Integer inviteeId) {
        this.inviteeId = inviteeId;
    }

    public Integer getInviterId() {
        return inviterId;
    }

    public void setInviterId(Integer inviterId) {
        this.inviterId = inviterId;
    }

    public String getInviterName() {
        return inviterName;
    }

    public void setInviterName(String inviterName) {
        this.inviterName = inviterName;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", inviteeId=").append(inviteeId);
        sb.append(", inviterId=").append(inviterId);
        sb.append(", inviterName=").append(inviterName);
        sb.append(", kind=").append(kind);
        sb.append(", count=").append(count);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}