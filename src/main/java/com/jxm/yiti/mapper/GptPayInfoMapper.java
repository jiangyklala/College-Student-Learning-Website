package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.GptPayInfo;
import com.jxm.yiti.domain.GptPayInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GptPayInfoMapper {
    long countByExample(GptPayInfoExample example);

    int deleteByExample(GptPayInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(GptPayInfo record);

    int insertSelective(GptPayInfo record);

    List<GptPayInfo> selectByExample(GptPayInfoExample example);

    GptPayInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") GptPayInfo record, @Param("example") GptPayInfoExample example);

    int updateByExample(@Param("record") GptPayInfo record, @Param("example") GptPayInfoExample example);

    int updateByPrimaryKeySelective(GptPayInfo record);

    int updateByPrimaryKey(GptPayInfo record);
}