package com.jxm.yiti.controller;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @Cacheable(cacheNames = "wxQuestion/selectAll", keyGenerator = "myKeyGenerator")
    @GetMapping("/selectAll")
    @ResponseBody
    public CommonResp<PageResp<WxQuestionQueryResp>> list(@Valid WxQuestionQueryReq req) {
        CommonResp<PageResp<WxQuestionQueryResp>> resp = new CommonResp<>();

        PageResp<WxQuestionQueryResp> list = wxQuestionService.selectAll(req, false);

        resp.setContent(list);
        log.info("没有获取到缓存, 更新缓存");
        return resp;
    }

    /**
     * Admin 下查询题库中的所有题目, 拦截器中不拦截
     *
     * @param req 查询参数
     */
    @GetMapping("/selectAllAdmin")
    @ResponseBody
    public CommonResp<PageResp<WxQuestionQueryResp>> selectAllAdmin(@Valid WxQuestionQueryReq req) {
        CommonResp<PageResp<WxQuestionQueryResp>> resp = new CommonResp<>();

        PageResp<WxQuestionQueryResp> list = wxQuestionService.selectAll(req, true);

        resp.setContent(list);
        return resp;
    }

    /**
     * 根据所属目录 id 查该目录下的所有题目列表
     */
    @Cacheable(cacheNames = "wxQuestion/selectByCategoryId", keyGenerator = "myKeyGenerator")
    @GetMapping("/selectByCategoryId")
    @ResponseBody
    public CommonResp<PageResp<WxQuestionQueryResp>> selectByCategoryId(@Valid WxQuestionQueryReq req) {
        CommonResp<PageResp<WxQuestionQueryResp>> resp = new CommonResp<>();

        PageResp<WxQuestionQueryResp> list = wxQuestionService.selectByCategoryId(req);

        resp.setContent(list);
        log.info("没有获取到缓存, 更新缓存");
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
    @Cacheable(cacheNames = "wxQuestion/selectAnswer", keyGenerator = "myKeyGenerator")
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
        log.info("没有获取到缓存, 更新缓存");
        return resp;
    }


    /**
     * 新增或者更新一个题目
     *
     * @param req 保存参数
     */
    @Caching(evict = {
            @CacheEvict(cacheNames = "wxQuestion/selectAnswer", allEntries = true),
            @CacheEvict(cacheNames = "wxQuestion/selectAll", allEntries = true),
            @CacheEvict(cacheNames = "wxQuestion/selectByCategoryId", allEntries = true)

    })
    @PostMapping("/save")
    @ResponseBody
    public CommonResp save(@RequestBody @Valid WxQuestionSaveReq req) {  // 以 json 方式提交
        CommonResp resp = new CommonResp();

        if (wxQuestionService.save(req) != 1) {
            resp.setSuccess(false);
            resp.setMessage("保存题目失败");
        }

        return resp;
    }


    /**
     * 删除一个 id = ? 的题目
     *
     * @param id 所删除题目的 id
     * @return CommonResp
     */
    @Caching(evict = {
            @CacheEvict(cacheNames = "wxQuestion/selectAnswer", allEntries = true),
            @CacheEvict(cacheNames = "wxQuestion/selectAll", allEntries = true),
            @CacheEvict(cacheNames = "wxQuestion/selectByCategoryId", allEntries = true)

    })
    @DeleteMapping("/delete")
    @ResponseBody
    public CommonResp delete(@RequestBody WxQuestionDelReq req) {  // 拿到 "/delete/{id}" 里的 id
        CommonResp2 resp = new CommonResp2();

        wxQuestionService.delete(resp, req);

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
