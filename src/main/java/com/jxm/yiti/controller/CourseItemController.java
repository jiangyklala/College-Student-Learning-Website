package com.jxm.yiti.controller;

import com.jxm.yiti.req.CourseItemQueryReq;
import com.jxm.yiti.req.CourseItemSaveReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.CourseItemQueryResp;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.service.CourseItemService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/courseItem")
public class CourseItemController {

    @Resource
    private CourseItemService courseItemService;

    /**
     * 查询所有课程视频
     *
     * @param req 查询参数
     */
    @GetMapping("/select")
    @ResponseBody
    public CommonResp list(@Valid CourseItemQueryReq req) {
        CommonResp<PageResp<CourseItemQueryResp>> resp = new CommonResp<>();

        PageResp<CourseItemQueryResp> list = courseItemService.select(req);

        resp.setContent(list);
        return resp;
    }

    /**
     * 新增或者更新一个课程视频
     *
     * @param req 保存参数
     * @return CommonResp
     */
    @PostMapping("/save")
    @ResponseBody
    public CommonResp save(@RequestBody @Valid CourseItemSaveReq req) {  // 以 json 方式提交
        CommonResp resp = new CommonResp();

        if (courseItemService.save(req) != 1) {
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

        if (courseItemService.delete(id) != 1) {
            resp.setSuccess(false);
            resp.setMessage("删除下载项失败");
        }

        return resp;
    }

}
