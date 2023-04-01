package com.jxm.yiti.controller;

import com.jxm.yiti.req.DownloadListQueryReq;
import com.jxm.yiti.req.DownloadListSaveReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.DownloadListQueryResp;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.service.DownloadListService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
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
     */
    @GetMapping("/selectAll")
    @ResponseBody
    public CommonResp list(@Valid DownloadListQueryReq req) {
        CommonResp<PageResp<DownloadListQueryResp>> resp = new CommonResp<>();

        PageResp<DownloadListQueryResp> list = downloadListService.selectAll(req);

        resp.setContent(list);
        return resp;
    }

    /**
     * 新增或者更新一个下载项
     *
     * @param req 保存参数
     */
    @PostMapping("/save")
    @ResponseBody
    public CommonResp save(@RequestBody @Valid DownloadListSaveReq req) {  // 以 json 方式提交
        CommonResp resp = new CommonResp();

        if (downloadListService.save(req) != 1) {
            resp.setSuccess(false);
            resp.setMessage("保存下载项失败");
        }

        return resp;
    }

    /**
     * 删除一个 id = ? 的下载项
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

    /**
     * 根据分类id查出对应的下载项, [-1] --- 查出全部下载项
     */
    @GetMapping("/selectByCategoryId")
    @ResponseBody
    public CommonResp selectByCategoryId(DownloadListQueryReq req) {
        CommonResp<PageResp<DownloadListQueryResp>> resp = new CommonResp<>();

        PageResp<DownloadListQueryResp> list = downloadListService.selectByCategoryId(req);

        resp.setContent(list);
        return resp;
    }

    /**
     * 为某个下载项的下载数自增 1
     * @param id 下载项 ID
     */
    @PostMapping("/incrDownloadCount/{id}")
    @ResponseBody
    public CommonResp incrDownloadCount(@PathVariable Long id) {
        CommonResp resp = new CommonResp();

        if (downloadListService.incrDownloadCount(id) != 1) {
            resp.setSuccess(false);
            resp.setMessage("点赞失败");
        }

        return resp;
    }

}
