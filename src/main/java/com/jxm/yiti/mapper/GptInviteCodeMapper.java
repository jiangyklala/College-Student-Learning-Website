package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.GptInviteCode;
import com.jxm.yiti.domain.GptInviteCodeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GptInviteCodeMapper {
    long countByExample(GptInviteCodeExample example);

    int deleteByExample(GptInviteCodeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(GptInviteCode record);

    int insertSelective(GptInviteCode record);

    List<GptInviteCode> selectByExample(GptInviteCodeExample example);

    GptInviteCode selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") GptInviteCode record, @Param("example") GptInviteCodeExample example);

    int updateByExample(@Param("record") GptInviteCode record, @Param("example") GptInviteCodeExample example);

    int updateByPrimaryKeySelective(GptInviteCode record);

    int updateByPrimaryKey(GptInviteCode record);
}