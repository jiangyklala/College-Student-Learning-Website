package com.jxm.yiti.mapper.cust;

public interface CategoryMapperCust {

    /**
     * 重置二级分类下的 "题目总数"
     */
    public void resetSecondCount();

    /**
     * 重置一级分类下的 "题目总数"
     */
    public void resetFirstCount();
}