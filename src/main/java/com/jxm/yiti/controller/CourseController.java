package com.jxm.yiti.controller;


import com.jxm.yiti.req.CourseListQueryReq;
import com.jxm.yiti.req.DownloadListQueryReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.CourseListQueryResp;
import com.jxm.yiti.resp.DownloadListQueryResp;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.service.CourseListService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/course")
public class CourseController {

    @Resource
    private CourseListService courseListService;

    /**
     * 查询课程列表的所有数据
     *
     * @param req 查询参数
     */
    @GetMapping("/list")
    @ResponseBody
    public CommonResp list(@Valid CourseListQueryReq req) {
        CommonResp<PageResp<CourseListQueryResp>> resp = new CommonResp<>();

        PageResp<CourseListQueryResp> list = courseListService.list(req);

        resp.setContent(list);
        return resp;
    }

}
