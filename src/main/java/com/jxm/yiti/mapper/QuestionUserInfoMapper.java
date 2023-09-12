package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.QuestionUserInfo;
import com.jxm.yiti.domain.QuestionUserInfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionUserInfoMapper {
    long countByExample(QuestionUserInfoExample example);

    int deleteByExample(QuestionUserInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(QuestionUserInfo record);

    int insertSelective(QuestionUserInfo record);

    List<QuestionUserInfo> selectByExampleWithBLOBs(QuestionUserInfoExample example);

    List<QuestionUserInfo> selectByExample(QuestionUserInfoExample example);

    QuestionUserInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") QuestionUserInfo record, @Param("example") QuestionUserInfoExample example);

    int updateByExampleWithBLOBs(@Param("record") QuestionUserInfo record, @Param("example") QuestionUserInfoExample example);

    int updateByExample(@Param("record") QuestionUserInfo record, @Param("example") QuestionUserInfoExample example);

    int updateByPrimaryKeySelective(QuestionUserInfo record);

    int updateByPrimaryKeyWithBLOBs(QuestionUserInfo record);

    int updateByPrimaryKey(QuestionUserInfo record);
}