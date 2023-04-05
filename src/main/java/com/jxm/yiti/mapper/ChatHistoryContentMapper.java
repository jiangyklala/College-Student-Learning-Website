package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.ChatHistoryContent;
import com.jxm.yiti.domain.ChatHistoryContentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ChatHistoryContentMapper {
    long countByExample(ChatHistoryContentExample example);

    int deleteByExample(ChatHistoryContentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ChatHistoryContent record);

    int insertSelective(ChatHistoryContent record);

    List<ChatHistoryContent> selectByExampleWithBLOBs(ChatHistoryContentExample example);

    List<ChatHistoryContent> selectByExample(ChatHistoryContentExample example);

    ChatHistoryContent selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ChatHistoryContent record, @Param("example") ChatHistoryContentExample example);

    int updateByExampleWithBLOBs(@Param("record") ChatHistoryContent record, @Param("example") ChatHistoryContentExample example);

    int updateByExample(@Param("record") ChatHistoryContent record, @Param("example") ChatHistoryContentExample example);

    int updateByPrimaryKeySelective(ChatHistoryContent record);

    int updateByPrimaryKeyWithBLOBs(ChatHistoryContent record);
}