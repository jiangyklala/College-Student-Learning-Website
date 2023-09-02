package com.jxm.yiti.mapper;

import com.jxm.yiti.domain.WxUserInfo;
import com.jxm.yiti.domain.WxUserInfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WxUserInfoMapper {
    long countByExample(WxUserInfoExample example);

    int deleteByExample(WxUserInfoExample example);

    int deleteByPrimaryKey(String openId);

    int insert(WxUserInfo record);

    int insertSelective(WxUserInfo record);

    List<WxUserInfo> selectByExample(WxUserInfoExample example);

    WxUserInfo selectByPrimaryKey(String openId);

    int updateByExampleSelective(@Param("record") WxUserInfo record, @Param("example") WxUserInfoExample example);

    int updateByExample(@Param("record") WxUserInfo record, @Param("example") WxUserInfoExample example);

    int updateByPrimaryKeySelective(WxUserInfo record);

    int updateByPrimaryKey(WxUserInfo record);
}