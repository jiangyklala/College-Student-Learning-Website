package com.jxm.yiti.controller;

import com.jxm.yiti.req.DownloadListReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.DownloadListResp;
import com.jxm.yiti.service.DownloadListService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/downloadList")
public class DownloadListController {

    @Resource
    private DownloadListService downloadListService;

    @GetMapping("/list")
    @ResponseBody
    public CommonResp list(DownloadListReq req) {
        CommonResp<List<DownloadListResp>> resp = new CommonResp<>();
        List<DownloadListResp> list = downloadListService.list(req);
        resp.setContent(list);
        return resp;
    }

}