package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.GptInvitee;
import com.jxm.yiti.domain.GptInviteeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GptInviteeMapper {
    long countByExample(GptInviteeExample example);

    int deleteByExample(GptInviteeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(GptInvitee record);

    int insertSelective(GptInvitee record);

    List<GptInvitee> selectByExample(GptInviteeExample example);

    GptInvitee selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") GptInvitee record, @Param("example") GptInviteeExample example);

    int updateByExample(@Param("record") GptInvitee record, @Param("example") GptInviteeExample example);

    int updateByPrimaryKeySelective(GptInvitee record);

    int updateByPrimaryKey(GptInvitee record);
}