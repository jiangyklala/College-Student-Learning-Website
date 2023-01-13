package com.jxm.yiti.controller;

import com.jxm.yiti.req.DownloadListQueryReq;
import com.jxm.yiti.req.DownloadListSaveReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.DownloadListQueryResp;
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

    /**
     * 查询下载列表的所有数据
     *
     * @param req 查询参数
     * @return CommonResq
     */
    @GetMapping("/list")
    @ResponseBody
    public CommonResp list(DownloadListQueryReq req) {
        CommonResp<PageResp<DownloadListQueryResp>> resp = new CommonResp<>();

        PageResp<DownloadListQueryResp> list = downloadListService.list(req);

        resp.setContent(list);
        return resp;
    }

    /**
     * 新增或者更新一个下载项
     *
     * @param req 保存参数
     * @return CommonResp
     */
    @PostMapping("/save")
    @ResponseBody
    public CommonResp save(@RequestBody DownloadListSaveReq req) {  // 以 json 方式提交
        CommonResp resp = new CommonResp();

        if (downloadListService.save(req) != 1) {
            resp.setSuccess(false);
            resp.setMessage("保存下载项失败");
        }

        return resp;
    }

    /**
     * 删除一个下载项
     *
     * @param id 所删除下载项的 id
     * @return CommonResp
     */
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public CommonResp delete(@PathVariable Long id) {  // 拿到 "/delete/{id}" 里的 id
        CommonResp resp = new CommonResp();

        if (downloadListService.delete(id) != 1) {
            resp.setSuccess(false);
            resp.setMessage("删除下载项失败");
        }

        return resp;
    }

}
