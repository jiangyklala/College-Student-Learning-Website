package com.jxm.yiti.resp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GptInviteCodeResp {
    private String inviteCode;

    private Date createTime;

    private String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return formatter.format(date);
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    @Override
    public String toString() {
        return "GptInviteCodeResp{" +
                "inviteCode='" + inviteCode + '\'' +
                ", creatTime=" + dateToString(createTime) +
                '}';
    }
}
