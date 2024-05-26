package com.jxm.yiti.controller.gpt;

import com.jxm.yiti.domain.ChatHistory;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.service.GptService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/gpt")
public class GptController {

    @Resource
    GptService gptService;

    /**
     * 查询某个用户下的所有历史记录
     * @param userID 用户 ID
     * @return
     */
    @GetMapping("/selectAllByID/{userID}")
    @ResponseBody
    public CommonResp selectAll(@PathVariable Long userID) {
        CommonResp<List<ChatHistory>> resp = new CommonResp<>();

        resp.setContent(gptService.selectAllByID(userID));

        return resp;
    }

    @GetMapping("/selectContentByID/{historyId}")
    @ResponseBody
    public CommonResp<String> selectContentByID(@PathVariable Long historyId) {
        CommonResp<String> resp = new CommonResp<>();
        resp.setContent(gptService.selectContentByID(historyId));
        return resp;
    }
}
