package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.ChatHistory;
import com.jxm.yiti.domain.ChatHistoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ChatHistoryMapper {
    long countByExample(ChatHistoryExample example);

    int deleteByExample(ChatHistoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ChatHistory record);

    int insertSelective(ChatHistory record);

    List<ChatHistory> selectByExample(ChatHistoryExample example);

    ChatHistory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ChatHistory record, @Param("example") ChatHistoryExample example);

    int updateByExample(@Param("record") ChatHistory record, @Param("example") ChatHistoryExample example);

    int updateByPrimaryKeySelective(ChatHistory record);

    int updateByPrimaryKey(ChatHistory record);
}