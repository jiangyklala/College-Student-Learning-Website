package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.EmailActive;
import com.jxm.yiti.domain.EmailActiveExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EmailActiveMapper {
    long countByExample(EmailActiveExample example);

    int deleteByExample(EmailActiveExample example);

    int deleteByPrimaryKey(String email);

    int insert(EmailActive record);

    int insertSelective(EmailActive record);

    List<EmailActive> selectByExample(EmailActiveExample example);

    EmailActive selectByPrimaryKey(String email);

    int updateByExampleSelective(@Param("record") EmailActive record, @Param("example") EmailActiveExample example);

    int updateByExample(@Param("record") EmailActive record, @Param("example") EmailActiveExample example);

    int updateByPrimaryKeySelective(EmailActive record);

    int updateByPrimaryKey(EmailActive record);
}