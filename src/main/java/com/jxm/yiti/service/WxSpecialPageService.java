package com.jxm.yiti.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxm.yiti.domain.WxSpecialPage;
import com.jxm.yiti.domain.WxSpecialPageExample;
import com.jxm.yiti.interceptor.WxAppInterceptor;
import com.jxm.yiti.mapper.WxSpecialPageMapper;
import com.jxm.yiti.req.WxSpecialPageQueryReq;
import com.jxm.yiti.req.WxSpecialPageSaveReq;
import com.jxm.yiti.resp.CommonResp2;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.resp.WxSpecialPageResp;
import com.jxm.yiti.utils.CopyUtil;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyunkai <jiangyunkai@kuaishou.com>
 * Created on 2024-02-18
 */
@Slf4j
@Service
public class WxSpecialPageService {

    @Resource
    WxSpecialPageMapper wxSpecialPageMapper;

    public PageResp<WxSpecialPageResp> selectAll(WxSpecialPageQueryReq req) {
        WxSpecialPageExample wxSpecialPageExample = new WxSpecialPageExample();
        WxSpecialPageExample.Criteria criteria = wxSpecialPageExample.createCriteria();
        // 是否查询某个题目
        if (!ObjectUtils.isEmpty(req.getTitle())) {
            criteria.andTitleLike("%" + req.getTitle() + "%");
        }

        PageHelper.startPage(req.getPage(), req.getSize(), true);
        List<WxSpecialPage> wxSpecialPageList = wxSpecialPageMapper.selectByExample(wxSpecialPageExample);

        PageInfo<WxSpecialPage> wxSpecialPagePageInfo = new PageInfo<>(wxSpecialPageList);
        log.debug("当前页: " + wxSpecialPagePageInfo.getPageNum()
                + ", 总页数: " + wxSpecialPagePageInfo.getPages()
                + " , 总记录数: " + wxSpecialPagePageInfo.getTotal());

        PageResp<WxSpecialPageResp> resp = new PageResp<>();
        resp.setList(CopyUtil.copyList(wxSpecialPageList, WxSpecialPageResp.class));
        resp.setTotal(wxSpecialPagePageInfo.getTotal());

        return resp;
    }

    public void selectOne(WxSpecialPageQueryReq req, CommonResp2<List<WxSpecialPageResp>> resp) {
        WxSpecialPageExample wxSpecialPageExample = new WxSpecialPageExample();
        WxSpecialPageExample.Criteria criteria = wxSpecialPageExample.createCriteria();
        criteria.andIdEqualTo(req.getId());
        // 是否查询某个题目
        if (!ObjectUtils.isEmpty(req.getTitle())) {
            criteria.andTitleLike("%" + req.getTitle() + "%");
        }

        try {
            List<WxSpecialPage> wxSpecialPageList = wxSpecialPageMapper.selectByExample(wxSpecialPageExample);
            resp.setContent(CopyUtil.copyList(wxSpecialPageList, WxSpecialPageResp.class));
        } catch (RuntimeException e) {
            resp.setCode(420);
            log.error("specialPage error, wxUserId:" + WxAppInterceptor.getWxUserId());
        }

    }

    public void save(WxSpecialPageSaveReq req, CommonResp2 resp) {
        WxSpecialPage res = CopyUtil.copy(req, WxSpecialPage.class);

        try {
            if (ObjectUtils.isEmpty(req.getId())) {
                // 新增
                wxSpecialPageMapper.insertSelective(res);
            } else {
                // 更新
                wxSpecialPageMapper.updateByPrimaryKeyWithBLOBs(res);
            }
        } catch (RuntimeException e) {
            log.error("specialPage error, wxUserId:" + WxAppInterceptor.getWxUserId());
            resp.setCode(420);
        }
    }
}
