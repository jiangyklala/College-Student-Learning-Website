package com.jxm.yiti.mapper.cust;

import org.apache.ibatis.annotations.Param;

public interface UserMapperCust {

    /**
     * 获取并且减少某个账户下的提问次数
     * @param id userID
     * @param count 需要减少的次数
     * @return (tmp)
     */
    public int balanceGetAndDecrNum(@Param("id") Long id, @Param("count") Long count);

    /**
     * 获取并且增加某个账户下的提问次数
     * @param id userID
     * @param count 需要增加的次数
     * @return (tmp)
     */
    public int balanceGetAndIncrNum(@Param("id") Long id, @Param("count") Long count);

    /**
     * 对某个邮箱增加提问次数
     * @param email 用户邮箱
     * @param count 需要增加的次数
     * @return (tmp)
     */
    public int payWithCount(@Param("userEmail") String email, @Param("count") Long count);
}
