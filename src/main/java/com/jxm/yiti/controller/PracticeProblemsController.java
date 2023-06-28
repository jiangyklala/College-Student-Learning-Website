package com.jxm.yiti.controller;

import com.jxm.yiti.req.PracticeSettingsSaveReq;
import com.jxm.yiti.req.QuestionDetailQueryReq;
import com.jxm.yiti.req.QuestionDetailSaveReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.resp.PracticeUserQueryResp;
import com.jxm.yiti.resp.QuestionDetailQueryResp;
import com.jxm.yiti.service.PracticeProblemsService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 保存(更新)用户的刷题设置
     *
     * @param req 保存参数
     */
    @PostMapping("/saveSettings")
    @ResponseBody
    public CommonResp saveSettings(@RequestBody @Valid PracticeSettingsSaveReq req) {  // 以 json 方式提交
        CommonResp resp = new CommonResp();

        practiceProblemsService.saveSettings(req, resp);

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

    /**
     * 查询所有题目, 按 categoryId1 分组, 按 categoryId2 升序排序
     */
    @GetMapping("/selectAllGpByCgId1")
    @ResponseBody
    public CommonResp selectAllGpByCgId2() {
        CommonResp<List<List<QuestionDetailQueryResp>>> resp = new CommonResp<>();

        List<List<QuestionDetailQueryResp>> list = practiceProblemsService.selectAllGpByCgId1();

        resp.setContent(list);
        return resp;
    }

    /**
     * 查询某个用户的刷题设置信息
     */
    @GetMapping("/selectPracticeUserInfo/{userID}")
    @ResponseBody
    public CommonResp selectPracticeUserInfo(@PathVariable Long userID) {
        CommonResp<PracticeUserQueryResp> resp = new CommonResp<>();

        PracticeUserQueryResp practiceUserQueryResp = practiceProblemsService.selectPracticeUserInfo(userID);
        resp.setContent(practiceUserQueryResp);

        return resp;
    }
}
