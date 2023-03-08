package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.ColumnList;
import com.jxm.yiti.domain.ColumnListExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ColumnListMapper {
    long countByExample(ColumnListExample example);

    int deleteByExample(ColumnListExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ColumnList record);

    int insertSelective(ColumnList record);

    List<ColumnList> selectByExample(ColumnListExample example);

    ColumnList selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ColumnList record, @Param("example") ColumnListExample example);

    int updateByExample(@Param("record") ColumnList record, @Param("example") ColumnListExample example);

    int updateByPrimaryKeySelective(ColumnList record);

    int updateByPrimaryKey(ColumnList record);
}