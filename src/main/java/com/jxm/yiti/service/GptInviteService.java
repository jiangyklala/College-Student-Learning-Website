package com.jxm.yiti.service;

import com.jxm.yiti.domain.GptInviteCode;
import com.jxm.yiti.domain.GptInviteCodeExample;
import com.jxm.yiti.domain.GptInviter;
import com.jxm.yiti.domain.GptInviterExample;
import com.jxm.yiti.mapper.GptInviteCodeMapper;
import com.jxm.yiti.mapper.GptInviteeMapper;
import com.jxm.yiti.mapper.GptInviterMapper;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.GptInviteCodeResp;
import com.jxm.yiti.resp.GptInviterResp;
import com.jxm.yiti.utils.CopyUtil;
import com.jxm.yiti.utils.InviteCodeGenerate;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class GptInviteService {

    @Resource
    GptInviterMapper gptInviterMapper;

    @Resource
    GptInviteCodeMapper gptInviteCodeMapper;

    @Resource
    GptInviteeMapper gptInviteeMapper;

    public void selectInfoByID(Long userID, CommonResp<GptInviterResp> resp) {
        GptInviter existInviter = isExistInviter(userID);

        // 不存在此用户
        if (existInviter == null) {
            existInviter = addInviter(userID, resp);
        }

        // 构造返回信息
        resp.setContent(CopyUtil.copy(existInviter, GptInviterResp.class));
    }

    public GptInviter addInviter(Long userID, CommonResp resp) {
        GptInviter gptInviter = new GptInviter();
        gptInviter.setInviterId(userID);
        gptInviter.setEarnings(0);
        gptInviter.setEarnRate(20);
        gptInviter.setInviteBalance(0);
        gptInviter.setInvitedCount(0);

        try {
            gptInviterMapper.insert(gptInviter);
        } catch (RuntimeException e) {
            resp.setSuccess(false);
            resp.setMessage("插入邀请人失败");
            log.error("插入邀请人失败", e);
        }

        return gptInviter;
    }

    public void generateInviteCode(Long userID, CommonResp<String> resp) {
        // 邀请人不存在, 则添加 (这里确实需要吗?? 如果用户点击生成邀请码, 说明已经经过了 selectInfoByID 中的判断, 但不排除某些卡 bug 的)
        if (isExistInviter(userID) == null) {
            addInviter(userID, resp);
        }

        String inviteCode = InviteCodeGenerate.next();
        GptInviteCode gptInviteCode = new GptInviteCode();
        gptInviteCode.setInviterId(userID);
        gptInviteCode.setInviteCode(inviteCode);
        gptInviteCode.setCreateTime(new Date());

        try {
            gptInviteCodeMapper.insert(gptInviteCode);
        } catch (RuntimeException e) {
            resp.setSuccess(false);
            resp.setMessage("生成邀请码失败");
            log.error("生成邀请码失败", e);
        }

        resp.setContent(inviteCode);
    }

    private GptInviter isExistInviter(Long userID) {
        List<GptInviter> gptInviter = null;

        GptInviterExample gptInviterExample = new GptInviterExample();
        GptInviterExample.Criteria criteria = gptInviterExample.createCriteria();
        criteria.andInviterIdEqualTo(userID);
        try {
            gptInviter = gptInviterMapper.selectByExample(gptInviterExample);
        } catch (RuntimeException e) {
            log.error("查找邀请人信息失败", e);
        }

        // 没有直接返回
        if (gptInviter == null || gptInviter.size() == 0) {
            return null;
        }

        return gptInviter.get(0);
    }

    public void selectAllInviteCode(Long userID, CommonResp<List<GptInviteCodeResp>> resp) {
        GptInviteCodeExample gptInviteCodeExample = new GptInviteCodeExample();
        GptInviteCodeExample.Criteria criteria = gptInviteCodeExample.createCriteria();
        criteria.andInviterIdEqualTo(userID);

        List<GptInviteCode> gptInviteCodes = gptInviteCodeMapper.selectByExample(gptInviteCodeExample);
        List<GptInviteCodeResp> gptInviteCodeResps = CopyUtil.copyList(gptInviteCodes, GptInviteCodeResp.class);

        resp.setContent(gptInviteCodeResps);
    }
}
