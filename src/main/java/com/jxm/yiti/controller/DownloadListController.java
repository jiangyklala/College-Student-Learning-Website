package com.jxm.yiti.controller;

import com.jxm.yiti.req.DownloadListQueryReq;
import com.jxm.yiti.req.DownloadListSaveReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.DownloadListResp;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.service.DownloadListService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/downloadList")
public class DownloadListController {

    @Resource
    private DownloadListService downloadListService;

    @GetMapping("/list")
    @ResponseBody
    public CommonResp list(DownloadListQueryReq req) {
        CommonResp<PageResp<DownloadListResp>> resp = new CommonResp<>();

        PageResp<DownloadListResp> list = downloadListService.list(req);

        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    @ResponseBody
    public CommonResp save(@RequestBody DownloadListSaveReq req) {  // 以 json 方式提交
        CommonResp resp = new CommonResp();

        if (downloadListService.save(req) != 1) {
            resp.setSuccess(false);
            resp.setMessage("保存下载列表的信息失败");
        }

        return resp;
    }

}
