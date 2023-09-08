package com.jxm.yiti.controller;

import com.jxm.yiti.req.WxQuestionQueryReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.resp.WxQuestionQueryResp;
import com.jxm.yiti.service.WxQuestionService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/wxQuestion")
public class WxQuestionController {

    @Resource
    WxQuestionService wxQuestionService;


    /**
     * 查询题库中的所有题目
     *
     * @param req 查询参数
     */
    @GetMapping("/selectAll")
    @ResponseBody
    public CommonResp list(@Valid WxQuestionQueryReq req) {
        CommonResp<PageResp<WxQuestionQueryResp>> resp = new CommonResp<>();

        PageResp<WxQuestionQueryResp> list = wxQuestionService.selectAll(req);

        resp.setContent(list);
        return resp;
    }

    @GetMapping("/selectByCategoryId")
    @ResponseBody
    public CommonResp selectByCategoryId(@Valid WxQuestionQueryReq req) {
        CommonResp<PageResp<WxQuestionQueryResp>> resp = new CommonResp<>();

        PageResp<WxQuestionQueryResp> list = wxQuestionService.selectByCategoryId(req);

        resp.setContent(list);
        return resp;
    }

    @GetMapping("/selectAnswer")
    @ResponseBody
    public CommonResp selectAnswer(Integer answerId) {
        CommonResp<String> resp = new CommonResp<>();

        String answer = wxQuestionService.selectAnswer(answerId);
        if (answer == null) {
            resp.setSuccess(false);
            resp.setMessage("查找问题答案失败");
        }

        resp.setContent(answer);
        return resp;
    }


}
