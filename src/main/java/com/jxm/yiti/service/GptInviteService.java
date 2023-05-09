package com.jxm.yiti.service;

import com.jxm.yiti.domain.*;
import com.jxm.yiti.mapper.GptInviteCodeMapper;
import com.jxm.yiti.mapper.GptInviteeMapper;
import com.jxm.yiti.mapper.GptInviterMapper;
import com.jxm.yiti.mapper.cust.GptInviterMapperCust;
import com.jxm.yiti.mapper.cust.UserMapperCust;
import com.jxm.yiti.req.UserQueryReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.GptInviteCodeResp;
import com.jxm.yiti.resp.GptInviterResp;
import com.jxm.yiti.utils.CopyUtil;
import com.jxm.yiti.utils.InviteCodeGenerate;
import com.jxm.yiti.utils.SnowFlakeIdWorker;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class GptInviteService {

    @Resource
    GptInviterMapper gptInviterMapper;

    @Resource
    GptInviteCodeMapper gptInviteCodeMapper;

    @Resource
    GptInviteeMapper gptInviteeMapper;

    @Resource
    UserMapperCust userMapperCust;

    @Resource
    GptInviterMapperCust gptInviterMapperCust;

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

    public void isInvite(UserQueryReq user, CommonResp resp) {
        log.info("inviteCode: {}", user.getInviteCode());
        if (!resp.getSuccess() || Objects.equals(user.getInviteCode(), "") || user.getInviteCode() == null) {
            return;
        }

        // 设置 userID / inviteeID
        user.setId(UserService.snowFlakeIdWorker.nextId());

        // 默认邀请码不会失效
        // TO-DO

        try {
            // 查找此邀请码对应的邀请人ID
            GptInviteCodeExample gptInviteCodeExample = new GptInviteCodeExample();
            GptInviteCodeExample.Criteria criteria = gptInviteCodeExample.createCriteria();
            criteria.andInviteCodeEqualTo(user.getInviteCode());
            List<GptInviteCode> gptInviteCodes = gptInviteCodeMapper.selectByExample(gptInviteCodeExample);
            if (gptInviteCodes.isEmpty()) {
                resp.setSuccess(false);
                resp.setMessage(resp.getMessage() + "邀请码不存在, 请确认或联系管理员");
                log.error("邀请码不存在: {}, inviteeId: {}", user.getInviteCode(), user.getId());
            }
            Long inviterId = gptInviteCodes.get(0).getInviterId();

            // 查找被邀请人的邀请利率
            GptInviter gptInviter = gptInviterMapper.selectByPrimaryKey(inviterId);
            long incrNum = UserService.initBalance * gptInviter.getEarnRate() / 100;

            // 增加被邀请人次数
            userMapperCust.balanceGetAndIncrNum(inviterId, incrNum);
            gptInviterMapperCust.invitedCountIncr(inviterId);

            // 将此账号与邀请人在 invitee 表中绑定, 并记录
            GptInvitee gptInvitee = new GptInvitee();
            gptInvitee.setInviteeId(user.getId());
            gptInvitee.setInviterId(inviterId);
            gptInvitee.setKind(0);
            gptInvitee.setCount((int) incrNum);
            gptInvitee.setCreateTime(new Date());
            gptInviteeMapper.insert(gptInvitee);
        } catch (RuntimeException e) {
            resp.setSuccess(false);
            resp.setMessage(resp.getMessage() + "邀请码不存在, 请确认或联系管理员");
            log.error("邀请码不存在: {}, inviteeId: {}", user.getInviteCode(), user.getId());
        }
    }
}
