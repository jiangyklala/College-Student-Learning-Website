package com.jxm.yiti.controller;


import com.jxm.yiti.req.DocQueryReq;
import com.jxm.yiti.req.DocSaveReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.DocQueryResp;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.service.DocService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/doc")
public class DocController {

    @Resource
    private DocService docService;

    /**
     * 查询专栏列表的所有数据
     *
     * @param req 查询参数
     */
    @GetMapping("/selectAll")
    @ResponseBody
    public CommonResp selectAll(@Valid DocQueryReq req) {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();

        List<DocQueryResp> list = docService.selectAll(req);

        resp.setContent(list);
        return resp;
    }

    /**
     * 根据 columnId 查询对应专栏
     */
    @GetMapping("/selectByColumnId/{columnId}")
    @ResponseBody
    public CommonResp selectByColumnId(@PathVariable Long columnId) {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();

        List<DocQueryResp> list = docService.selectByColumnId(columnId);

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
    public CommonResp save(@RequestBody @Valid DocSaveReq req) {  // 以 json 方式提交
        CommonResp resp = new CommonResp();

        if (docService.save(req) != 1) {
            resp.setSuccess(false);
            resp.setMessage("保存下载项失败");
        }

        return resp;
    }

    /**
     * 删除一个专栏
     *
     * @param id 所删除专栏项的 id
     * @return CommonResp
     */
    @DeleteMapping("/deleteId/{id}")
    @ResponseBody
    public CommonResp deleteId(@PathVariable Long id) {  // 拿到 "/delete/{id}" 里的 id
        CommonResp resp = new CommonResp();

        if (docService.deleteId(id) != 1) {
            resp.setSuccess(false);
            resp.setMessage("删除下载项失败");
        }

        return resp;
    }
    /**
     * 删除多个专栏
     *
     * @param idStr 所删除专栏的 Id 字符串, 多个专栏 Id 之间用 ',' 分割
     * @return CommonResp
     */
    @DeleteMapping("/deleteIdStr/{idStr}")
    @ResponseBody
    public CommonResp deleteIdStr(@PathVariable String idStr) {  // 拿到 "/delete/{id}" 里的 id
        CommonResp resp = new CommonResp();

        if (docService.deleteStr(idStr) == 0) {
            resp.setSuccess(false);
            resp.setMessage("删除下载项失败");
        }

        return resp;
    }

}
