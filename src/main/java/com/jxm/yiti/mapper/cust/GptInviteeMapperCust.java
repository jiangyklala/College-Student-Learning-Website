package com.jxm.yiti.mapper.cust;

import com.jxm.yiti.domain.GptInvitee;
import com.jxm.yiti.domain.GptInviteeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GptInviteeMapperCust {

    List<GptInvitee> selectByInviteeId(@Param("invitee_id") Long userId);

}