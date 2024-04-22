package com.jxm.yiti.mapper.cust;

import org.apache.ibatis.annotations.Param;

public interface WxInviteCodeMapperCust {
    /**
     * 对已经邀请的人数自增
     *
     * @param inviteCode 邀请码
     */
    public Long existsInviteCode(@Param("inviteCode") String inviteCode);

}