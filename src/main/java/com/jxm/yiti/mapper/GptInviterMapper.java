package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.GptInviter;
import com.jxm.yiti.domain.GptInviterExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GptInviterMapper {
    long countByExample(GptInviterExample example);

    int deleteByExample(GptInviterExample example);

    int deleteByPrimaryKey(Long id);

    int insert(GptInviter record);

    int insertSelective(GptInviter record);

    List<GptInviter> selectByExample(GptInviterExample example);

    GptInviter selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") GptInviter record, @Param("example") GptInviterExample example);

    int updateByExample(@Param("record") GptInviter record, @Param("example") GptInviterExample example);

    int updateByPrimaryKeySelective(GptInviter record);

    int updateByPrimaryKey(GptInviter record);
}