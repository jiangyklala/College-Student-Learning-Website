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

    /**
     * 找到 level = ?, parent = ? 的最大 sort 字段的值
     */
    public Integer findSortMax(Integer level, Integer parent);
}