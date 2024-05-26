package com.jxm.yiti.controller.early;

import com.jxm.yiti.req.RecruitInfoReq;
import com.jxm.yiti.resp.CommonResp2;
import com.jxm.yiti.resp.RecruitInfoResp;
import com.jxm.yiti.service.OnlineTableService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/onlineTable")
public class OnlineTableController {

    @Resource
    OnlineTableService onlineTableService;

    @GetMapping("/selectAll")
    @ResponseBody
    public CommonResp2<List<RecruitInfoResp>> selectAll() {
        CommonResp2<List<RecruitInfoResp>> resp = new CommonResp2<>();

        onlineTableService.selectAll(resp);

        return resp;
    }

    @PostMapping("/save")
    @ResponseBody
    public CommonResp2 save(RecruitInfoReq req) {
        CommonResp2 resp = new CommonResp2();

        onlineTableService.save(req, resp);

        return resp;
    }
}
