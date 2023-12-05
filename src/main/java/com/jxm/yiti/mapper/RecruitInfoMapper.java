package com.jxm.yiti.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxm.yiti.domain.RecruitInfo;
import com.jxm.yiti.domain.RecruitInfoExample;

public interface RecruitInfoMapper {
    long countByExample(RecruitInfoExample example);

    int deleteByExample(RecruitInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RecruitInfo record);

    int insertSelective(RecruitInfo record);

    List<RecruitInfo> selectByExampleWithBLOBs(RecruitInfoExample example);

    List<RecruitInfo> selectByExample(RecruitInfoExample example);

    RecruitInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RecruitInfo record, @Param("example") RecruitInfoExample example);

    int updateByExampleWithBLOBs(@Param("record") RecruitInfo record, @Param("example") RecruitInfoExample example);

    int updateByExample(@Param("record") RecruitInfo record, @Param("example") RecruitInfoExample example);

    int updateByPrimaryKeySelective(RecruitInfo record);

    int updateByPrimaryKeyWithBLOBs(RecruitInfo record);

    int updateByPrimaryKey(RecruitInfo record);
}