package com.jxm.yiti.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxm.yiti.domain.WxQuestion;
import com.jxm.yiti.domain.WxQuestionAnswer;
import com.jxm.yiti.domain.WxQuestionExample;
import com.jxm.yiti.mapper.QuestionUserInfoMapper;
import com.jxm.yiti.mapper.WxQuestionAnswerMapper;
import com.jxm.yiti.mapper.WxQuestionMapper;
import com.jxm.yiti.mapper.cust.QuestionUserInfoMapperCust;
import com.jxm.yiti.req.WxQuestionDelReq;
import com.jxm.yiti.req.WxQuestionQueryReq;
import com.jxm.yiti.req.WxQuestionSaveReq;
import com.jxm.yiti.resp.CommonResp2;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.resp.WxQuestionQueryResp;
import com.jxm.yiti.utils.CopyUtil;
import com.jxm.yiti.utils.SnowFlakeIdWorker;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@Service
public class WxQuestionService {

    @Resource
    WxQuestionMapper wxQuestionMapper;

    @Resource
    WxQuestionAnswerMapper wxQuestionAnswerMapper;

    @Resource
    QuestionUserInfoMapper questionUserInfoMapper;

    @Resource
    QuestionUserInfoMapperCust questionUserInfoMapperCust;

    @Resource
    SnowFlakeIdWorker snowFlakeIdWorker;

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

    public String selectAnswer(Long answerId) {
        WxQuestionAnswer wxQuestionAnswer = null;

        try {
            wxQuestionAnswer = wxQuestionAnswerMapper.selectByPrimaryKey(answerId);
        } catch (Exception e) {
            log.info("查找文档内容失败");
            return null;
        }

        return wxQuestionAnswer.getAnswer();
    }

    public int save(WxQuestionSaveReq req) {
        WxQuestion res = CopyUtil.copy(req, WxQuestion.class);

        try {
            if (ObjectUtils.isEmpty(req.getId())) {
                // 先插入答案表
                long answerId = snowFlakeIdWorker.nextId();
                WxQuestionAnswer wxQuestionAnswer = new WxQuestionAnswer();
                wxQuestionAnswer.setId(answerId);
                wxQuestionAnswer.setAnswer(req.getAnswer());
                wxQuestionAnswerMapper.insert(wxQuestionAnswer);

                // 插入题目表
                res.setAnswerId(answerId);
                return wxQuestionMapper.insertSelective(res);
            } else {
                // 先更新答案表
                WxQuestionAnswer wxQuestionAnswer = new WxQuestionAnswer();
                wxQuestionAnswer.setAnswer(req.getAnswer());
                wxQuestionAnswer.setId(res.getAnswerId());
                wxQuestionAnswerMapper.updateByPrimaryKeyWithBLOBs(wxQuestionAnswer);

                // 再更新题目表

                return wxQuestionMapper.updateByPrimaryKey(res);
            }
        } catch (DataIntegrityViolationException e) {
            log.info("错误: 插入或更新错误");
            e.printStackTrace();
            return -1;
        }

    }

    public void delete(CommonResp2 resp, WxQuestionDelReq req) {
        try {
            // 先删除答案记录
            wxQuestionAnswerMapper.deleteByPrimaryKey(req.getWxQuestionAnswerId());

            // 再删除题目记录
            wxQuestionMapper.deleteByPrimaryKey(req.getWxQuestionId());

            log.info("题目删除: wxQuestionAnswerId: {}, wxQuestionId: {}", req.getWxQuestionAnswerId(), req.getWxQuestionId());
        } catch (RuntimeException e) {
            resp.setCode(420);
            e.printStackTrace();
        }
    }
}
