package com.jxm.yiti.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxm.yiti.domain.WxInvitee;
import com.jxm.yiti.domain.WxInviteeExample;

public interface WxInviteeMapper {
    long countByExample(WxInviteeExample example);

    int deleteByExample(WxInviteeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WxInvitee record);

    int insertSelective(WxInvitee record);

    List<WxInvitee> selectByExample(WxInviteeExample example);

    WxInvitee selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WxInvitee record, @Param("example") WxInviteeExample example);

    int updateByExample(@Param("record") WxInvitee record, @Param("example") WxInviteeExample example);

    int updateByPrimaryKeySelective(WxInvitee record);

    int updateByPrimaryKey(WxInvitee record);
}