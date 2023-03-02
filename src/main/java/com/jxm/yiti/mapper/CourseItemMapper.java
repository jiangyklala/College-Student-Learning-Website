package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.CourseItem;
import com.jxm.yiti.domain.CourseItemExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CourseItemMapper {
    long countByExample(CourseItemExample example);

    int deleteByExample(CourseItemExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CourseItem record);

    int insertSelective(CourseItem record);

    List<CourseItem> selectByExample(CourseItemExample example);

    CourseItem selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CourseItem record, @Param("example") CourseItemExample example);

    int updateByExample(@Param("record") CourseItem record, @Param("example") CourseItemExample example);

    int updateByPrimaryKeySelective(CourseItem record);

    int updateByPrimaryKey(CourseItem record);
}