package com.jxm.yiti.mapper.cust;

import com.jxm.yiti.domain.QuestionDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionDetailMapperCust {

    List<QuestionDetail> selectProblemsExc(@Param("ids") List<Long> excludedIds, @Param("count") Integer count);

    List<QuestionDetail> selectProblemsInc(@Param("ids") List<Long> includedIds, @Param("count") Integer count);

}