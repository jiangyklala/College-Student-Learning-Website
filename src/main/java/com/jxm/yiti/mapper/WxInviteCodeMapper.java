package com.jxm.yiti.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxm.yiti.domain.WxInviteCode;
import com.jxm.yiti.domain.WxInviteCodeExample;

public interface WxInviteCodeMapper {
    long countByExample(WxInviteCodeExample example);

    int deleteByExample(WxInviteCodeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WxInviteCode record);

    int insertSelective(WxInviteCode record);

    List<WxInviteCode> selectByExample(WxInviteCodeExample example);

    WxInviteCode selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WxInviteCode record, @Param("example") WxInviteCodeExample example);

    int updateByExample(@Param("record") WxInviteCode record, @Param("example") WxInviteCodeExample example);

    int updateByPrimaryKeySelective(WxInviteCode record);

    int updateByPrimaryKey(WxInviteCode record);
}