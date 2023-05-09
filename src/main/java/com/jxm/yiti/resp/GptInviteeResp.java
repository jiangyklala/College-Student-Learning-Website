package com.jxm.yiti.resp;

import java.util.Date;

public class GptInviteeResp {
    private Integer kind;

    private Integer count;

    private Date createTime;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", kind=").append(kind);
        sb.append(", count=").append(count);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}