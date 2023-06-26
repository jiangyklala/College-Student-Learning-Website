package com.jxm.yiti.controller;

import com.jxm.yiti.req.CategoryQueryReq;
import com.jxm.yiti.req.CategorySaveReq;
import com.jxm.yiti.resp.CategoryQueryResp;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.service.CategoryService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 查询下载列表的所有数据
     *
     * @param req 查询参数
     */
    @GetMapping("/selectAll")
    @ResponseBody
    public CommonResp list(@Valid CategoryQueryReq req) {
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();

        PageResp<CategoryQueryResp> list = categoryService.selectAll(req);

        resp.setContent(list);
        return resp;
    }

    /**
     * 查询分类列表的所有分类, 并根据 sort 字段排序
     */
    @GetMapping("/selectAllOBSort")
    @ResponseBody
    public CommonResp selectAllOBSort() {
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();

        List<CategoryQueryResp> list = categoryService.selectAllOBSort();

        resp.setContent(list);
        return resp;
    }

    /**
     * 查询分类列表关于刷题的分类, 并根据 sort 字段排序
     */
    @GetMapping("/selectPracticeOBSort")
    @ResponseBody
    public CommonResp selectPracticeOBSort() {
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();

        List<CategoryQueryResp> list = categoryService.selectPracticeOBSort();

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
    public CommonResp save(@RequestBody @Valid CategorySaveReq req) {  // 以 json 方式提交
        CommonResp resp = new CommonResp();

        if (categoryService.save(req) != 1) {
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

        if (categoryService.delete(id) != 1) {
            resp.setSuccess(false);
            resp.setMessage("删除下载项失败");
        }

        return resp;
    }

}
