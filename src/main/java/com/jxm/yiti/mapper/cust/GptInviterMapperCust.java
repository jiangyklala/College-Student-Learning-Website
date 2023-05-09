package com.jxm.yiti.mapper.cust;

import org.apache.ibatis.annotations.Param;

public interface GptInviterMapperCust {

    /**
     * 对已经邀请的人数自增
     * @param id userID
     * @return (tmp)
     */
    public int invitedCountIncr(@Param("id") Long id);

}
