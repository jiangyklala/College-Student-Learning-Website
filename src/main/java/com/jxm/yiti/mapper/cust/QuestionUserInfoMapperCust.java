package com.jxm.yiti.mapper.cust;


import com.jxm.yiti.domain.QuestionUserInfo;
import org.apache.ibatis.annotations.Param;

public interface QuestionUserInfoMapperCust {
    QuestionUserInfo selectByUserId(@Param("userId") Integer userId);
}