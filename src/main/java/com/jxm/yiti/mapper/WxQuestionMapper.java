package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.WxQuestion;
import com.jxm.yiti.domain.WxQuestionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WxQuestionMapper {
    long countByExample(WxQuestionExample example);

    int deleteByExample(WxQuestionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WxQuestion record);

    int insertSelective(WxQuestion record);

    List<WxQuestion> selectByExampleWithBLOBs(WxQuestionExample example);

    List<WxQuestion> selectByExample(WxQuestionExample example);

    WxQuestion selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WxQuestion record, @Param("example") WxQuestionExample example);

    int updateByExampleWithBLOBs(@Param("record") WxQuestion record, @Param("example") WxQuestionExample example);

    int updateByExample(@Param("record") WxQuestion record, @Param("example") WxQuestionExample example);

    int updateByPrimaryKeySelective(WxQuestion record);

    int updateByPrimaryKeyWithBLOBs(WxQuestion record);

    int updateByPrimaryKey(WxQuestion record);
}