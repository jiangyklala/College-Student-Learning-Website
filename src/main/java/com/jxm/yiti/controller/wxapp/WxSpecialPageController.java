package com.jxm.yiti.controller.wxapp;

import com.jxm.yiti.annotation.AccessLimit;
import com.jxm.yiti.enums.WxUserConst;
import com.jxm.yiti.req.WxSpecialPageQueryReq;
import com.jxm.yiti.req.WxSpecialPageSaveReq;
import com.jxm.yiti.resp.CommonResp2;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.resp.WxSpecialPageResp;
import com.jxm.yiti.service.WxSpecialPageService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author jiangyunkai <jiangyunkai@kuaishou.com>
 * Created on 2024-02-18
 */
@Slf4j
@Controller
@RequestMapping("/wxSpecial")
public class WxSpecialPageController {

    @Resource
    WxSpecialPageService wxSpecialPageService;

    @GetMapping("/selectAll")
    @ResponseBody
    public CommonResp2<PageResp<WxSpecialPageResp>> selectAll(@Valid WxSpecialPageQueryReq req) {
        CommonResp2<PageResp<WxSpecialPageResp>> resp = new CommonResp2<>();

        PageResp<WxSpecialPageResp> list = wxSpecialPageService.selectAll(req);

        resp.setContent(list);
        return resp;
    }

    @GetMapping("/selectAllAdmin")
    @ResponseBody
    public CommonResp2<PageResp<WxSpecialPageResp>> selectAllAdmin(@Valid WxSpecialPageQueryReq req) {
        CommonResp2<PageResp<WxSpecialPageResp>> resp = new CommonResp2<>();

        PageResp<WxSpecialPageResp> list = wxSpecialPageService.selectAll(req);

        resp.setContent(list);
        return resp;
    }

    @AccessLimit(type = {WxUserConst.NORMAL_USER, WxUserConst.NORMAL_VIP, WxUserConst.SPECIAL_VIP, WxUserConst.SUPER})
    @GetMapping("/selectOne")
    @ResponseBody
    public CommonResp2<WxSpecialPageResp> selectOne(@Valid WxSpecialPageQueryReq req) {
        CommonResp2<WxSpecialPageResp> resp = new CommonResp2<>();

        wxSpecialPageService.selectOne(req, resp);

        return resp;
    }

    @PostMapping("/save")
    @ResponseBody
    public CommonResp2 save(@RequestBody WxSpecialPageSaveReq req) {
        CommonResp2 resp = new CommonResp2();

        wxSpecialPageService.save(req, resp);

        return resp;
    }
}
