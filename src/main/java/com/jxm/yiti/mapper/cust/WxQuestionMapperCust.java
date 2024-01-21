package com.jxm.yiti.mapper.cust;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.jxm.yiti.domain.WxQuestion;

public interface WxQuestionMapperCust {
    public List<WxQuestion> selectUserMarked(@Param("answerIdSet") Set<Long> answerIdSet);
}