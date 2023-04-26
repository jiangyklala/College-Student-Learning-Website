package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.ChatRecordInfo;
import com.jxm.yiti.domain.ChatRecordInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ChatRecordInfoMapper {
    long countByExample(ChatRecordInfoExample example);

    int deleteByExample(ChatRecordInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ChatRecordInfo record);

    int insertSelective(ChatRecordInfo record);

    List<ChatRecordInfo> selectByExample(ChatRecordInfoExample example);

    ChatRecordInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ChatRecordInfo record, @Param("example") ChatRecordInfoExample example);

    int updateByExample(@Param("record") ChatRecordInfo record, @Param("example") ChatRecordInfoExample example);

    int updateByPrimaryKeySelective(ChatRecordInfo record);

    int updateByPrimaryKey(ChatRecordInfo record);
}