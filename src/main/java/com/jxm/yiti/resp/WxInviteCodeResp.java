package com.jxm.yiti.resp;

import java.util.Date;

import lombok.Data;

@Data
public class WxInviteCodeResp {

    private String inviteCode;

    private Integer inviterId;

    private Date createTime;
}