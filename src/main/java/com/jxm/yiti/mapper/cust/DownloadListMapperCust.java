package com.jxm.yiti.mapper.cust;

import org.apache.ibatis.annotations.Param;


public interface DownloadListMapperCust {

    public int incrDownloadCount(@Param("id") Long id);

}