package com.jxm.yiti.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxm.yiti.domain.WxInviter;
import com.jxm.yiti.domain.WxInviterExample;

public interface WxInviterMapper {
    long countByExample(WxInviterExample example);

    int deleteByExample(WxInviterExample example);

    int deleteByPrimaryKey(Integer inviterId);

    int insert(WxInviter record);

    int insertSelective(WxInviter record);

    List<WxInviter> selectByExample(WxInviterExample example);

    WxInviter selectByPrimaryKey(Integer inviterId);

    int updateByExampleSelective(@Param("record") WxInviter record, @Param("example") WxInviterExample example);

    int updateByExample(@Param("record") WxInviter record, @Param("example") WxInviterExample example);

    int updateByPrimaryKeySelective(WxInviter record);

    int updateByPrimaryKey(WxInviter record);
}