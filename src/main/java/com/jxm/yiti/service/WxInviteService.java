package com.jxm.yiti.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.jxm.yiti.domain.WxInviteCode;
import com.jxm.yiti.domain.WxInviteCodeExample;
import com.jxm.yiti.domain.WxInviter;
import com.jxm.yiti.domain.WxInviterExample;
import com.jxm.yiti.enums.StatusCode;
import com.jxm.yiti.mapper.WxInviteCodeMapper;
import com.jxm.yiti.mapper.WxInviteeMapper;
import com.jxm.yiti.mapper.WxInviterMapper;
import com.jxm.yiti.mapper.cust.WxInviteCodeMapperCust;
import com.jxm.yiti.resp.CheckInviteCodeResp;
import com.jxm.yiti.resp.CommonResp2;
import com.jxm.yiti.resp.WxInviteCodeResp;
import com.jxm.yiti.resp.WxInviterResp;
import com.jxm.yiti.utils.CopyUtil;
import com.jxm.yiti.utils.InviteCodeGenerate;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyunkai <jiangyunkai@kuaishou.com>
 * Created on 2024-04-05
 */
@Slf4j
@Service
public class WxInviteService {

    @Resource
    WxInviteCodeMapper wxInviteCodeMapper;

    @Resource
    WxInviterMapper wxInviterMapper;

    @Resource
    WxInviteeMapper wxInviteeMapper;

    @Resource
    OtherService otherService;


    @Resource
    WxInviteCodeMapperCust wxInviteCodeMapperCust;

    public void selectInfo(Integer wxUserId, CommonResp2<WxInviterResp> resp) {
        WxInviter existInviter = isExistInviter(wxUserId);

        // 不存在此用户
        if (existInviter == null) {
            existInviter = addInviter(wxUserId, resp);
        }

        // 构造返回信息
        WxInviterResp wxInviterResp = CopyUtil.copy(existInviter, WxInviterResp.class);
        resp.setContent(wxInviterResp);
    }

    public void selectAllInviteCode(Integer wxUserId, CommonResp2<WxInviteCodeResp> resp) {
        WxInviteCode existInviteCode = isExistInviteCode(wxUserId);

        // 邀请码不存在, 自动添加一个邀请码
        if (existInviteCode == null) {
            existInviteCode = addInviteCode(wxUserId, resp);
        }

        resp.setContent(CopyUtil.copy(existInviteCode, WxInviteCodeResp.class));
    }

    private WxInviter addInviter(Integer wxUserId, CommonResp2<WxInviterResp> resp) {
        WxInviter wxInviter = new WxInviter();
        wxInviter.setInviterId(wxUserId);
        wxInviter.setEarnings(BigDecimal.valueOf(0));
        wxInviter.setEarnRate(20);
        wxInviter.setInviteBalance(BigDecimal.valueOf(0));
        wxInviter.setInvitedCount(0);

        try {
            wxInviterMapper.insert(wxInviter);
        } catch (RuntimeException e) {
            resp.setCode(420);
            resp.setSuccess(false);
            resp.setMessage("插入邀请人失败");
            log.error("插入邀请人失败", e);
            return null;
        }

        return wxInviter;
    }

    private WxInviter isExistInviter(Integer wxUserId) {
        List<WxInviter> wxInviters = null;

        WxInviterExample wxInviterExample = new WxInviterExample();
        WxInviterExample.Criteria criteria = wxInviterExample.createCriteria();
        criteria.andInviterIdEqualTo(wxUserId);
        try {
            wxInviters = wxInviterMapper.selectByExample(wxInviterExample);
        } catch (RuntimeException e) {
            log.error("查找邀请人信息失败", e);
        }

        // 没有直接返回
        if (wxInviters == null || wxInviters.size() == 0) {
            return null;
        }

        return wxInviters.get(0);
    }

    private WxInviteCode addInviteCode(Integer wxUserId, CommonResp2<WxInviteCodeResp> resp) {
        String inviteCode = generateInviteCode(resp);

        if (!resp.getSuccess()) {
            return null;
        }

        WxInviteCode wxInviteCode = new WxInviteCode();
        wxInviteCode.setInviteCode(inviteCode);
        wxInviteCode.setInviterId(wxUserId);
        wxInviteCode.setCreateTime(new Date());

        try {
            wxInviteCodeMapper.insert(wxInviteCode);
        } catch (RuntimeException e) {
            resp.setCode(420);
            resp.setSuccess(false);
            resp.setMessage("插入邀请Code失败");
            log.error("插入邀请Code失败", e);
            return null;
        }

        return wxInviteCode;
    }

    private String generateInviteCode(CommonResp2<WxInviteCodeResp> resp) {
        String inviteCode = InviteCodeGenerate.next();

        // 检测邀请码是否重复
        Long existsInviteCode = wxInviteCodeMapperCust.existsInviteCode(inviteCode);
        log.debug("existsInviteCode: {}", existsInviteCode);
        int count = 0;
        while (existsInviteCode != 0) {
            log.warn("邀请码重复, 重新生成中");
            inviteCode = InviteCodeGenerate.next();
            existsInviteCode = wxInviteCodeMapperCust.existsInviteCode(inviteCode);
            if (++count > 10) {
                log.error("邀请码已重复生成: {}, 自动退出", count);
                resp.setSuccess(false);
                resp.setCode(420);
                resp.setMessage("邀请码生成失败, 请重试或者联系客服");
            }
        }

        return inviteCode;
    }

    /**
     * 查询某个用户是否存在邀请码
     */
    private WxInviteCode isExistInviteCode(Integer wxUserId) {
        List<WxInviteCode> wxInviteCodes = null;

        WxInviteCodeExample wxInviteCodeExample = new WxInviteCodeExample();
        WxInviteCodeExample.Criteria criteria = wxInviteCodeExample.createCriteria();
        criteria.andInviterIdEqualTo(wxUserId);

        try {
            wxInviteCodes = wxInviteCodeMapper.selectByExample(wxInviteCodeExample);
        } catch (RuntimeException e) {
            log.error("查找邀请Code信息失败", e);
        }

        // 没有直接返回
        if (wxInviteCodes == null || wxInviteCodes.size() == 0) {
            return null;
        }

        return wxInviteCodes.get(0);
    }

    /**
     * 查询某个邀请码是否有效
     */
    public void checkInviteCode(Integer wxUserId, String inviteCode, CommonResp2<CheckInviteCodeResp> resp) {
        CheckInviteCodeResp inviteCodeResp = new CheckInviteCodeResp();
        List<WxInviteCode> wxInviteCodes = null;

        WxInviteCodeExample wxInviteCodeExample = new WxInviteCodeExample();
        WxInviteCodeExample.Criteria criteria = wxInviteCodeExample.createCriteria();
        criteria.andInviteCodeEqualTo(inviteCode);

        try {
            wxInviteCodes = wxInviteCodeMapper.selectByExample(wxInviteCodeExample);
        } catch (RuntimeException e) {
            log.error("查找邀请Code信息失败, inviteCode:{}", inviteCode, e);
        }

        if (wxInviteCodes == null || wxInviteCodes.size() == 0) {
            resp.setSuccess(false);
            resp.setCode(StatusCode.BUSINESS_BUSY.code);
            return;
        }

        WxInviteCode wxInviteCode = wxInviteCodes.get(0);
        WxInviter existInviter = isExistInviter(wxInviteCode.getInviterId());
        // 检查被邀请人是否具有分销权限, 并且被邀请人不能是自己
        if (!existInviter.getIsAccessible() || Objects.equals(wxUserId, wxInviteCode.getInviterId())) {
            resp.setSuccess(false);
            resp.setCode(StatusCode.BUSINESS_BUSY.code);
            return;
        }

        inviteCodeResp.setInviterId(existInviter.getInviterId());
        resp.setContent(inviteCodeResp);
    }
}
