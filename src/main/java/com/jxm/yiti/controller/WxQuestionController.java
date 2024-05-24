package com.jxm.yiti.controller;

import com.alibaba.fastjson2.JSON;
import com.jxm.yiti.annotation.AccessLimit;
import com.jxm.yiti.enums.WxUserConst;
import com.jxm.yiti.req.WxQuestionDelReq;
import com.jxm.yiti.req.WxQuestionFeedbackReq;
import com.jxm.yiti.req.WxQuestionQueryReq;
import com.jxm.yiti.req.WxQuestionSaveReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.CommonResp2;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.resp.WxQuestionQueryResp;
import com.jxm.yiti.service.WxQuestionService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/wxQuestion")
public class WxQuestionController {

    @Resource
    WxQuestionService wxQuestionService;

    /**
     * 查询题库中的所有题目, 拦截器中拦截
     *
     * @param req 查询参数
     */
    @Cacheable(cacheNames = "wxQuestion_selectAll_",
            key = "#req.categoryId + '_' + #req.categoryIdList",
            unless = "#result == null")
    @GetMapping("/selectAll")
    @ResponseBody
    public CommonResp<PageResp<WxQuestionQueryResp>> list(@Valid WxQuestionQueryReq req) {
        CommonResp<PageResp<WxQuestionQueryResp>> resp = new CommonResp<>();

        PageResp<WxQuestionQueryResp> list = wxQuestionService.selectAll(req, false);

        resp.setContent(list);
        log.info("WxQuestionController list, 没有获取到缓存, 更新缓存, key: {} value: {}",
                "wxQuestion_selectAll_" + req.getCategoryId() + '_' + req.getCategoryIdList(),
                JSON.toJSONString(list));
        return resp;
    }

    /**
     * Admin 下查询题库中的所有题目, 拦截器中不拦截
     *
     * @param req 查询参数
     */
    @PostMapping("/selectAllAdmin")
    @ResponseBody
    public CommonResp<PageResp<WxQuestionQueryResp>> selectAllAdmin(@RequestBody @Valid WxQuestionQueryReq req) {
        CommonResp<PageResp<WxQuestionQueryResp>> resp = new CommonResp<>();

        PageResp<WxQuestionQueryResp> list = wxQuestionService.selectAll(req, true);

        resp.setContent(list);
        return resp;
    }

    /**
     * 根据所属目录 id 查该目录下的所有题目列表
     */
    @Cacheable(cacheNames = "wxQuestion_selectByCategoryId_",
            key = "#req.categoryId",
            unless = "#result == null")
    @GetMapping("/selectByCategoryId")
    @ResponseBody
    public CommonResp<PageResp<WxQuestionQueryResp>> selectByCategoryId(@Valid WxQuestionQueryReq req) {
        CommonResp<PageResp<WxQuestionQueryResp>> resp = new CommonResp<>();

        PageResp<WxQuestionQueryResp> list = wxQuestionService.selectByCategoryId(req);

        resp.setContent(list);
        log.info("WxQuestionController selectByCategoryId, 没有获取到缓存, 更新缓存, key: {} value:{}",
                "wxQuestion_selectByCategoryId_" + req.getCategoryId(), JSON.toJSONString(list));
        return resp;
    }

    /**
     * 根据某个题目的答案 id 查该题的答案 Admin
     */
    @GetMapping("/selectAnswerAdmin")
    @ResponseBody
    public CommonResp selectAnswerAdmin(@RequestParam("answerId") Long answerId) {
        CommonResp<String> resp = new CommonResp<>();

        String answer = wxQuestionService.selectAnswer(answerId);
        if (answer == null) {
            resp.setSuccess(false);
            resp.setMessage("查找问题答案失败");
        }

        resp.setContent(answer);
        return resp;
    }

    /**
     * 根据某个题目的答案 id 查该题的答案
     */
    @Cacheable(cacheNames = "wxQuestion_selectAnswer_",
            key = "#answerId",
            unless = "#result == null")
    @AccessLimit(type = {WxUserConst.NORMAL_VIP, WxUserConst.SPECIAL_VIP, WxUserConst.SUPER})
    @GetMapping("/selectAnswer")
    @ResponseBody
    public CommonResp selectAnswer(@RequestParam("answerId") Long answerId) {
        CommonResp<String> resp = new CommonResp<>();

        String answer = wxQuestionService.selectAnswer(answerId);
        if (answer == null) {
            resp.setSuccess(false);
            resp.setMessage("查找问题答案失败");
        }

        resp.setContent(answer);
        log.info("WxQuestionController selectAnswer, 没有获取到缓存, 更新缓存, key: {}, value: {}",
                "wxQuestion_selectAnswer_" + answerId, JSON.toJSONString(resp));
        return resp;
    }


    /**
     * 新增或者更新一个题目
     *
     * @param req 保存参数
     */
    @Caching(evict = {
            @CacheEvict(cacheNames = "wxQuestion_selectAll_", allEntries = true),
            @CacheEvict(cacheNames = "wxQuestion_selectByCategoryId_", allEntries = true),
            @CacheEvict(cacheNames = "wxQuestion_selectAnswer_", allEntries = true)

    })
    @PostMapping("/save")
    @ResponseBody
    public CommonResp save(@RequestBody @Valid WxQuestionSaveReq req) {  // 以 json 方式提交
        CommonResp resp = new CommonResp();

        if (wxQuestionService.save(req) != 1) {
            resp.setSuccess(false);
            resp.setMessage("保存题目失败");
        }

        log.info("WxQuestionController save, 删除所有缓存");
        return resp;
    }


    /**
     * 删除一个 id = ? 的题目
     *
     * @param id 所删除题目的 id
     * @return CommonResp
     */
    @Caching(evict = {
            @CacheEvict(cacheNames = "wxQuestion_selectAll_", allEntries = true),
            @CacheEvict(cacheNames = "wxQuestion_selectByCategoryId_", allEntries = true),
            @CacheEvict(cacheNames = "wxQuestion_selectAnswer_", allEntries = true)
    })
    @DeleteMapping("/delete")
    @ResponseBody
    public CommonResp delete(@RequestBody WxQuestionDelReq req) {  // 拿到 "/delete/{id}" 里的 id
        CommonResp2 resp = new CommonResp2();

        wxQuestionService.delete(resp, req);

        log.info("WxQuestionController delete, 删除所有缓存");
        return resp;
    }

    /**
     * 反馈
     */
    @PostMapping("/feedback")
    @ResponseBody
    public CommonResp2 feedback(@RequestBody @Valid WxQuestionFeedbackReq req) {  // 以 json 方式提交
        CommonResp2 resp = new CommonResp2();

        wxQuestionService.feedback(req, resp);

        return resp;
    }

}
