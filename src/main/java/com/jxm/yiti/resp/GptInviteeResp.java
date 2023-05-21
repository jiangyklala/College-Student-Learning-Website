package com.jxm.yiti.resp;

import java.util.Date;

public class GptInviteeResp {
    private Integer kind;

    private String count;

    private Date createTime;

    private String inviterName;

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
        return "GptInviteeResp{" +
                "kind=" + kind +
                ", count=" + count +
                ", createTime=" + createTime +
                ", inviterName='" + inviterName + '\'' +
                '}';
    }
}