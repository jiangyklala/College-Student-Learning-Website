package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.DocContent;
import com.jxm.yiti.domain.DocContentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DocContentMapper {
    long countByExample(DocContentExample example);

    int deleteByExample(DocContentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DocContent record);

    int insertSelective(DocContent record);

    List<DocContent> selectByExampleWithBLOBs(DocContentExample example);

    List<DocContent> selectByExample(DocContentExample example);

    DocContent selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DocContent record, @Param("example") DocContentExample example);

    int updateByExampleWithBLOBs(@Param("record") DocContent record, @Param("example") DocContentExample example);

    int updateByExample(@Param("record") DocContent record, @Param("example") DocContentExample example);

    int updateByPrimaryKeySelective(DocContent record);

    int updateByPrimaryKeyWithBLOBs(DocContent record);
}