package com.jxm.yiti.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxm.yiti.domain.WxCollect;
import com.jxm.yiti.domain.WxCollectExample;

public interface WxCollectMapper {
    long countByExample(WxCollectExample example);

    int deleteByExample(WxCollectExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WxCollect record);

    int insertSelective(WxCollect record);

    List<WxCollect> selectByExampleWithBLOBs(WxCollectExample example);

    List<WxCollect> selectByExample(WxCollectExample example);

    WxCollect selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WxCollect record, @Param("example") WxCollectExample example);

    int updateByExampleWithBLOBs(@Param("record") WxCollect record, @Param("example") WxCollectExample example);

    int updateByExample(@Param("record") WxCollect record, @Param("example") WxCollectExample example);

    int updateByPrimaryKeySelective(WxCollect record);

    int updateByPrimaryKeyWithBLOBs(WxCollect record);

    int updateByPrimaryKey(WxCollect record);
}