package com.jxm.yiti.controller;

import com.jxm.yiti.req.QuestionDetailQueryReq;
import com.jxm.yiti.req.DownloadListSaveReq;
import com.jxm.yiti.req.QuestionDetailSaveReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.QuestionDetailQueryResp;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.service.PracticeProblemsService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/practice")
public class PracticeProblemsController {

    @Resource
    private PracticeProblemsService practiceProblemsService;


    /**
     * 查询下载列表的所有数据
     *
     * @param req 查询参数
     */
    @GetMapping("/selectAll")
    @ResponseBody
    public CommonResp list(@Valid QuestionDetailQueryReq req) {
        CommonResp<PageResp<QuestionDetailQueryResp>> resp = new CommonResp<>();

        PageResp<QuestionDetailQueryResp> list = practiceProblemsService.selectAll(req);

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
    public CommonResp save(@RequestBody @Valid QuestionDetailSaveReq req) {  // 以 json 方式提交
        CommonResp resp = new CommonResp();

        if (practiceProblemsService.save(req) != 1) {
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

        if (practiceProblemsService.delete(id) != 1) {
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
    public CommonResp selectByCategoryId(QuestionDetailQueryReq req) {
        CommonResp<PageResp<QuestionDetailQueryResp>> resp = new CommonResp<>();

        PageResp<QuestionDetailQueryResp> list = practiceProblemsService.selectByCategoryId(req);

        resp.setContent(list);
        return resp;
    }

}
