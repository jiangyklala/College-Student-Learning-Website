package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.CourseList;
import com.jxm.yiti.domain.CourseListExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CourseListMapper {
    long countByExample(CourseListExample example);

    int deleteByExample(CourseListExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CourseList record);

    int insertSelective(CourseList record);

    List<CourseList> selectByExample(CourseListExample example);

    CourseList selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CourseList record, @Param("example") CourseListExample example);

    int updateByExample(@Param("record") CourseList record, @Param("example") CourseListExample example);

    int updateByPrimaryKeySelective(CourseList record);

    int updateByPrimaryKey(CourseList record);
}