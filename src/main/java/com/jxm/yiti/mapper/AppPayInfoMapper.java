package com.jxm.yiti.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxm.yiti.domain.AppPayInfo;
import com.jxm.yiti.domain.AppPayInfoExample;

public interface AppPayInfoMapper {
    long countByExample(AppPayInfoExample example);

    int deleteByExample(AppPayInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AppPayInfo record);

    int insertSelective(AppPayInfo record);

    List<AppPayInfo> selectByExample(AppPayInfoExample example);

    AppPayInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AppPayInfo record, @Param("example") AppPayInfoExample example);

    int updateByExample(@Param("record") AppPayInfo record, @Param("example") AppPayInfoExample example);

    int updateByPrimaryKeySelective(AppPayInfo record);

    int updateByPrimaryKey(AppPayInfo record);
}