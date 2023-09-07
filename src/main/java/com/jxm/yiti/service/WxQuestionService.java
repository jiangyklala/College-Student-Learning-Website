package com.jxm.yiti.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxm.yiti.domain.WxQuestion;
import com.jxm.yiti.domain.WxQuestionExample;
import com.jxm.yiti.mapper.WxQuestionMapper;
import com.jxm.yiti.req.WxQuestionQueryReq;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.resp.WxQuestionQueryResp;
import com.jxm.yiti.utils.CopyUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@Service
public class WxQuestionService {

    @Resource
    WxQuestionMapper wxQuestionMapper;

    public PageResp<WxQuestionQueryResp> selectAll(WxQuestionQueryReq req) {
        WxQuestionExample wxQuestionExample = new WxQuestionExample();
        WxQuestionExample.Criteria criteria = wxQuestionExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getTitle())) {                                      // 动态 SQL
            criteria.andTitleLike("%" + req.getTitle() + "%");                            // 模糊匹配条件
        }

        PageHelper.startPage(req.getPage(), req.getSize(), true);
        List<WxQuestion> wxQuestionList = wxQuestionMapper.selectByExample(wxQuestionExample);

        PageInfo<WxQuestion> wxQuestionPageInfo = new PageInfo<>(wxQuestionList);
        log.info("当前页: " + wxQuestionPageInfo.getPageNum()
                + ", 总页数: " + wxQuestionPageInfo.getPages()
                + " , 总记录数: " + wxQuestionPageInfo.getTotal());

        PageResp<WxQuestionQueryResp> resp = new PageResp<>();
        resp.setList(CopyUtil.copyList(wxQuestionList, WxQuestionQueryResp.class));
        resp.setTotal(wxQuestionPageInfo.getTotal());

        return resp;
    }

    public PageResp<WxQuestionQueryResp> selectByCategoryId(WxQuestionQueryReq req) {
        WxQuestionExample wxQuestionExample = new WxQuestionExample();
        WxQuestionExample.Criteria criteria = wxQuestionExample.createCriteria();
        criteria.andCategoryIdEqualTo(req.getCategoryId());

        PageHelper.startPage(req.getPage(), req.getSize(), true);
        List<WxQuestion> wxQuestionList = wxQuestionMapper.selectByExample(wxQuestionExample);

        PageInfo<WxQuestion> wxQuestionPageInfo = new PageInfo<>(wxQuestionList);
        log.info("当前页: " + wxQuestionPageInfo.getPageNum()
                + ", 总页数: " + wxQuestionPageInfo.getPages()
                + " , 总记录数: " + wxQuestionPageInfo.getTotal());

        PageResp<WxQuestionQueryResp> resp = new PageResp<>();
        resp.setList(CopyUtil.copyList(wxQuestionList, WxQuestionQueryResp.class));
        resp.setTotal(wxQuestionPageInfo.getTotal());

        return resp;
    }
}
