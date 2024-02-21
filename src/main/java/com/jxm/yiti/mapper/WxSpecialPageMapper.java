package com.jxm.yiti.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxm.yiti.domain.WxSpecialPage;
import com.jxm.yiti.domain.WxSpecialPageExample;

public interface WxSpecialPageMapper {
    long countByExample(WxSpecialPageExample example);

    int deleteByExample(WxSpecialPageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WxSpecialPage record);

    int insertSelective(WxSpecialPage record);

    List<WxSpecialPage> selectByExampleWithBLOBs(WxSpecialPageExample example);

    List<WxSpecialPage> selectByExample(WxSpecialPageExample example);

    WxSpecialPage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WxSpecialPage record, @Param("example") WxSpecialPageExample example);

    int updateByExampleWithBLOBs(@Param("record") WxSpecialPage record, @Param("example") WxSpecialPageExample example);

    int updateByExample(@Param("record") WxSpecialPage record, @Param("example") WxSpecialPageExample example);

    int updateByPrimaryKeySelective(WxSpecialPage record);

    int updateByPrimaryKeyWithBLOBs(WxSpecialPage record);

    int updateByPrimaryKey(WxSpecialPage record);
}