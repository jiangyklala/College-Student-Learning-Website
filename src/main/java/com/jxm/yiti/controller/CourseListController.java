package com.jxm.yiti.controller;


import com.jxm.yiti.req.CourseListQueryReq;
import com.jxm.yiti.req.CourseListSaveReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.CourseListQueryResp;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.service.CourseListService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/courseList")
public class CourseListController {

    @Resource
    private CourseListService courseListService;

    /**
     * 查询课程列表的所有数据
     *
     * @param req 查询参数
     */
    @GetMapping("/selectAll")
    @ResponseBody
    public CommonResp list(@Valid CourseListQueryReq req) {
        CommonResp<PageResp<CourseListQueryResp>> resp = new CommonResp<>();

        PageResp<CourseListQueryResp> list = courseListService.selectAll(req);

        resp.setContent(list);
        return resp;
    }

    /**
     * 查询所有课程, 按 categoryId2 分组, 按 categoryId2 升序排序
     */
    @GetMapping("/selectAllGpByCgId2")
    @ResponseBody
    public CommonResp selectAllGpByCgId2() {
        CommonResp<List<List<CourseListQueryResp>>> resp = new CommonResp<>();

        List<List<CourseListQueryResp>> list = courseListService.selectAllGpByCgId2();

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
    public CommonResp save(@RequestBody @Valid CourseListSaveReq req) {  // 以 json 方式提交
        CommonResp resp = new CommonResp();

        if (courseListService.save(req) != 1) {
            resp.setSuccess(false);
            resp.setMessage("保存下载项失败");
        }

        return resp;
    }

    /**
     * 删除一个课程项
     *
     * @param id 所删除课程项的 id
     * @return CommonResp
     */
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public CommonResp delete(@PathVariable Long id) {  // 拿到 "/delete/{id}" 里的 id
        CommonResp resp = new CommonResp();

        if (courseListService.delete(id) != 1) {
            resp.setSuccess(false);
            resp.setMessage("删除下载项失败");
        }

        return resp;
    }

}
