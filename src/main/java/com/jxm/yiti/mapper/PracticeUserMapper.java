package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.PracticeUser;
import com.jxm.yiti.domain.PracticeUserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PracticeUserMapper {
    long countByExample(PracticeUserExample example);

    int deleteByExample(PracticeUserExample example);

    int deleteByPrimaryKey(Long userId);

    int insert(PracticeUser record);

    int insertSelective(PracticeUser record);

    List<PracticeUser> selectByExampleWithBLOBs(PracticeUserExample example);

    List<PracticeUser> selectByExample(PracticeUserExample example);

    PracticeUser selectByPrimaryKey(Long userId);

    int updateByExampleSelective(@Param("record") PracticeUser record, @Param("example") PracticeUserExample example);

    int updateByExampleWithBLOBs(@Param("record") PracticeUser record, @Param("example") PracticeUserExample example);

    int updateByExample(@Param("record") PracticeUser record, @Param("example") PracticeUserExample example);

    int updateByPrimaryKeySelective(PracticeUser record);

    int updateByPrimaryKeyWithBLOBs(PracticeUser record);
}