package com.jxm.yiti.service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.jxm.yiti.domain.WxCollect;
import com.jxm.yiti.domain.WxQuestion;
import com.jxm.yiti.domain.cust.WxCollectBLOB;
import com.jxm.yiti.mapper.WxCollectMapper;
import com.jxm.yiti.mapper.cust.WxCollectMapperCust;
import com.jxm.yiti.mapper.cust.WxQuestionMapperCust;
import com.jxm.yiti.req.WxCollectAddSingleOneReq;
import com.jxm.yiti.resp.CommonResp2;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WxCollectService {

    @Resource
    WxCollectMapper wxCollectMapper;

    @Resource
    WxCollectMapperCust wxCollectMapperCust;

    @Resource
    WxQuestionMapperCust wxQuestionMapperCust;

    private final static Integer ADD_FLAG = 1;

    private final static Integer RM_FLAG = 2;


    public void selectUserCollectInfo(CommonResp2<Set<Long>> resp, Integer wxUserId) {

        try {
            resp.setContent(selectAnswerIdSetByUserId(wxUserId));
        } catch (Exception e) {
            log.error("error", e);
            resp.setSuccess(false);
            resp.setCode(420);
        }
    }

    public void selectOneAllQuestion(CommonResp2<List<WxQuestion>> resp, Integer wxUserId) {

        try {
            Set<Long> collectSet = selectAnswerIdSetByUserId(wxUserId);
            if (collectSet == null || collectSet.isEmpty()) {
                resp.setContent(new LinkedList<>());
                return;
            }
            List<WxQuestion> wxQuestions = wxQuestionMapperCust.selectUserMarked(collectSet);
            resp.setContent(wxQuestions);
        } catch (Exception e) {
            log.error("error", e);
            resp.setSuccess(false);
            resp.setCode(420);
        }
    }

    @Nullable
    private Set<Long> selectAnswerIdSetByUserId(Integer wxUserId) {
        WxCollectBLOB wxCollectBLOB = wxCollectMapperCust.selectByUserIdWithBLOBsSingle(wxUserId);
        byte[] collectBlobs = (wxCollectBLOB != null) ? wxCollectBLOB.getCollectIdSet() : null;

        if (ObjectUtils.isEmpty(collectBlobs)) {
            // 该用户无记录, 添加新记录
            WxCollect wxCollect = insertNewItem(wxUserId);
            collectBlobs = wxCollect.getCollectIdSet();
        }

        TypeReference<Set<Long>> typeReference = new TypeReference<>() {
        };
        Set<Long> collectSet = JSON.parseObject(collectBlobs, typeReference.getType());
        return collectSet;
    }

    public void addOneCollect(WxCollectAddSingleOneReq req, CommonResp2 resp, Integer wxUserId) {
        updateOneCollect(req, resp, ADD_FLAG, wxUserId);
    }

    public void removeOneCollect(WxCollectAddSingleOneReq req, CommonResp2 resp, Integer wxUserId) {
        updateOneCollect(req, resp, RM_FLAG, wxUserId);
    }

    /**
     * 插入新记录, 并返回所插入内容
     */
    private WxCollect insertNewItem(Integer wxUserId) throws RuntimeException {
        WxCollect wxCollect = new WxCollect();
        wxCollect.setUserId(wxUserId);
        wxCollect.setCollectIdSet(JSON.toJSONBytes(new HashSet<Long>()));

        try {
            wxCollectMapper.insertSelective(wxCollect);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        return wxCollect;
    }

    private void updateOneCollect(WxCollectAddSingleOneReq req, CommonResp2 resp, Integer flag, Integer wxUserId) {
        try {
            WxCollect wxCollect = wxCollectMapperCust.selectByUserIdWithBLOBs(wxUserId);
            log.debug("wxUserId: {}, wxCollect: {}", wxUserId, wxCollect);

            // 考虑该用户从来没有访问过 #selectOne 接口 (即没有该用户收藏记录), 就添加收藏题目了
            if (wxCollect == null) {
                wxCollect = insertNewItem(wxUserId);
            }

            TypeReference<Set<Long>> typeReference = new TypeReference<>() {
            };
            Set<Long> collect = JSON.parseObject(wxCollect.getCollectIdSet(), typeReference.getType());

            if (Objects.equals(flag, ADD_FLAG)) {
                collect.add(req.getAnswerId());
            } else if (Objects.equals(flag, RM_FLAG)) {
                collect.remove(req.getAnswerId());
            }

            wxCollect.setCollectIdSet(JSON.toJSONBytes(collect));
            wxCollectMapperCust.updateByUserIdWithBLOBs(wxCollect);
        } catch (Exception e) {
            log.error("error", e);
            resp.setSuccess(false);
            resp.setCode(420);
        }
    }
}
