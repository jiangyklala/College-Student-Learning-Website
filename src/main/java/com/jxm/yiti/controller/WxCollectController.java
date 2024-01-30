package com.jxm.yiti.controller;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxm.yiti.domain.WxQuestion;
import com.jxm.yiti.interceptor.WxAppInterceptor;
import com.jxm.yiti.req.WxCollectAddSingleOneReq;
import com.jxm.yiti.resp.CommonResp2;
import com.jxm.yiti.service.WxCollectService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/wxCollect")
public class WxCollectController {

    @Resource
    WxCollectService wxCollectService;


    @PostMapping("/selectOne")
    @ResponseBody
    public CommonResp2<Set<Long>> selectOne() {
        CommonResp2<Set<Long>> resp = new CommonResp2<>();

        wxCollectService.selectUserCollectInfo(resp, WxAppInterceptor.getWxUserId());

        return resp;
    }


    @PostMapping("/selectOneAllQuestion")
    @ResponseBody
    public CommonResp2<List<WxQuestion>> selectOneAllQuestion() {
        CommonResp2<List<WxQuestion>> resp = new CommonResp2<>();

        wxCollectService.selectOneAllQuestion(resp, WxAppInterceptor.getWxUserId());

        return resp;
    }

    @PostMapping("/addOne")
    @ResponseBody
    public CommonResp2 addOne(@RequestBody WxCollectAddSingleOneReq req) {
        CommonResp2 resp = new CommonResp2();

        wxCollectService.addOneCollect(req, resp, WxAppInterceptor.getWxUserId());

        return resp;
    }

    @PostMapping("/removeOne")
    @ResponseBody
    public CommonResp2 removeOne(@RequestBody WxCollectAddSingleOneReq req) {
        CommonResp2 resp = new CommonResp2();

        wxCollectService.removeOneCollect(req, resp, WxAppInterceptor.getWxUserId());

        return resp;
    }


}
