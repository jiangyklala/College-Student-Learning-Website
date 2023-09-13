package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.WxQuestionAnswer;
import com.jxm.yiti.domain.WxQuestionAnswerExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WxQuestionAnswerMapper {
    long countByExample(WxQuestionAnswerExample example);

    int deleteByExample(WxQuestionAnswerExample example);

    int deleteByPrimaryKey(Long id);

    int insert(WxQuestionAnswer record);

    int insertSelective(WxQuestionAnswer record);

    List<WxQuestionAnswer> selectByExampleWithBLOBs(WxQuestionAnswerExample example);

    List<WxQuestionAnswer> selectByExample(WxQuestionAnswerExample example);

    WxQuestionAnswer selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") WxQuestionAnswer record, @Param("example") WxQuestionAnswerExample example);

    int updateByExampleWithBLOBs(@Param("record") WxQuestionAnswer record, @Param("example") WxQuestionAnswerExample example);

    int updateByExample(@Param("record") WxQuestionAnswer record, @Param("example") WxQuestionAnswerExample example);

    int updateByPrimaryKeySelective(WxQuestionAnswer record);

    int updateByPrimaryKeyWithBLOBs(WxQuestionAnswer record);
}