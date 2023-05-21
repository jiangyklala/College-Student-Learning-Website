package com.jxm.yiti.mapper.cust;

import org.apache.ibatis.annotations.Param;

public interface ChatHistoryMapperCust {

    int deleteChatHistory(@Param("user_id") Long userId, @Param("remain_count") Integer remainCount);

}