package com.jxm.yiti.domain;

import java.util.Date;

public class GptInvitee {
    private Long id;

    private Long inviteeId;

    private Long inviterId;

    private Integer kind;

    private Integer count;

    private Date createTime;

    private String inviterName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInviteeId() {
        return inviteeId;
    }

    public void setInviteeId(Long inviteeId) {
        this.inviteeId = inviteeId;
    }

    public Long getInviterId() {
        return inviterId;
    }

    public void setInviterId(Long inviterId) {
        this.inviterId = inviterId;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getInviterName() {
        return inviterName;
    }

    public void setInviterName(String inviterName) {
        this.inviterName = inviterName;
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
        sb.append(", kind=").append(kind);
        sb.append(", count=").append(count);
        sb.append(", createTime=").append(createTime);
        sb.append(", inviterName=").append(inviterName);
        sb.append("]");
        return sb.toString();
    }
}