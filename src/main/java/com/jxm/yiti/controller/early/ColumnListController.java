package com.jxm.yiti.controller.early;

import com.jxm.yiti.req.ColumnListQueryReq;
import com.jxm.yiti.req.ColumnListSaveReq;
import com.jxm.yiti.resp.ColumnListQueryResp;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.service.ColumnListService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/columnList")
public class ColumnListController {

    @Resource
    private ColumnListService columnListService;


    /**
     * 查询下载列表的所有数据
     *
     * @param req 查询参数
     */
    @GetMapping("/selectAll")
    @ResponseBody
    public CommonResp list(@Valid ColumnListQueryReq req) {
        CommonResp<PageResp<ColumnListQueryResp>> resp = new CommonResp<>();

        PageResp<ColumnListQueryResp> list = columnListService.selectAll(req);

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
    public CommonResp save(@RequestBody @Valid ColumnListSaveReq req) {  // 以 json 方式提交
        CommonResp resp = new CommonResp();

        if (columnListService.save(req) != 1) {
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

        if (columnListService.delete(id) != 1) {
            resp.setSuccess(false);
            resp.setMessage("删除下载项失败");
        }

        return resp;
    }

    /**
     * 根据分类id查出对应的下载项, [-1] --- 查出全部下载项
     */
    @GetMapping("/selectByCategoryId2")
    @ResponseBody
    public CommonResp selectByCategoryId(ColumnListQueryReq req) {
        CommonResp<List<ColumnListQueryResp>> resp = new CommonResp<>();

        List<ColumnListQueryResp> list = columnListService.selectByCategoryId2(req);

        resp.setContent(list);
        return resp;
    }

    /**
     * 查询所有课程, 按 categoryId2 分组, 按 categoryId2 升序排序
     */
    @GetMapping("/selectAllGpByCgId2")
    @ResponseBody
    public CommonResp selectAllGpByCgId2() {
        CommonResp<List<List<ColumnListQueryResp>>> resp = new CommonResp<>();

        List<List<ColumnListQueryResp>> list = columnListService.selectAllGpByCgId2();

        resp.setContent(list);
        return resp;
    }
}
