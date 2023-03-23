package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.DisallowWord;
import com.jxm.yiti.domain.DisallowWordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DisallowWordMapper {
    long countByExample(DisallowWordExample example);

    int deleteByExample(DisallowWordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DisallowWord record);

    int insertSelective(DisallowWord record);

    List<DisallowWord> selectByExample(DisallowWordExample example);

    DisallowWord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DisallowWord record, @Param("example") DisallowWordExample example);

    int updateByExample(@Param("record") DisallowWord record, @Param("example") DisallowWordExample example);

    int updateByPrimaryKeySelective(DisallowWord record);

    int updateByPrimaryKey(DisallowWord record);
}