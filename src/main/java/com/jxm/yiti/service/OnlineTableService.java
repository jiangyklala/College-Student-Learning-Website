package com.jxm.yiti.service;

import static com.jxm.yiti.enums.StatusCode.DB_ERROR;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import com.jxm.yiti.domain.RecruitInfo;
import com.jxm.yiti.mapper.RecruitInfoMapper;
import com.jxm.yiti.req.RecruitInfoReq;
import com.jxm.yiti.resp.CommonResp2;
import com.jxm.yiti.resp.RecruitInfoResp;
import com.jxm.yiti.utils.CopyUtil;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OnlineTableService {

    @Resource
    RecruitInfoMapper recruitInfoMapper;


    public void selectAll(CommonResp2<List<RecruitInfoResp>> resp) {

        List<RecruitInfo> recruitInfos;
        try {
            recruitInfos = recruitInfoMapper.selectByExample(null);
        } catch (Exception e) {
            log.error("DB ERROR", e);
            resp.setCode(DB_ERROR.code);
            return;
        }

        resp.setContent(CopyUtil.copyList(recruitInfos, RecruitInfoResp.class));
    }

    public void save(RecruitInfoReq req, CommonResp2 resp) {
        RecruitInfo recruitInfo = CopyUtil.copy(req, RecruitInfo.class);
        try {
            if (ObjectUtils.isEmpty(req.getId())) {
                recruitInfoMapper.insertSelective(recruitInfo);
            } else {
                recruitInfoMapper.updateByPrimaryKey(recruitInfo);
            }
        } catch (Exception e) {
            log.error("DB ERROR", e);
            resp.setCode(DB_ERROR.code);
            return;
        }
    }
}
