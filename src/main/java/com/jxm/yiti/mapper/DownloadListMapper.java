package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.DownloadList;
import com.jxm.yiti.domain.DownloadListExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DownloadListMapper {
    long countByExample(DownloadListExample example);

    int deleteByExample(DownloadListExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DownloadList record);

    int insertSelective(DownloadList record);

    List<DownloadList> selectByExample(DownloadListExample example);

    DownloadList selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DownloadList record, @Param("example") DownloadListExample example);

    int updateByExample(@Param("record") DownloadList record, @Param("example") DownloadListExample example);

    int updateByPrimaryKeySelective(DownloadList record);

    int updateByPrimaryKey(DownloadList record);
}