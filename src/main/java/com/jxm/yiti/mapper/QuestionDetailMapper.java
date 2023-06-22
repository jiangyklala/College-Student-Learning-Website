package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.QuestionDetail;
import com.jxm.yiti.domain.QuestionDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface QuestionDetailMapper {
    long countByExample(QuestionDetailExample example);

    int deleteByExample(QuestionDetailExample example);

    int deleteByPrimaryKey(Long id);

    int insert(QuestionDetail record);

    int insertSelective(QuestionDetail record);

    List<QuestionDetail> selectByExample(QuestionDetailExample example);

    QuestionDetail selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") QuestionDetail record, @Param("example") QuestionDetailExample example);

    int updateByExample(@Param("record") QuestionDetail record, @Param("example") QuestionDetailExample example);

    int updateByPrimaryKeySelective(QuestionDetail record);

    int updateByPrimaryKey(QuestionDetail record);
}