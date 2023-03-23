package com.jxm.yiti.controller;

import com.jxm.yiti.domain.DisallowWord;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.service.DisallowWordService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/disallowWord")
public class DisallowWordController {

    @Resource
    private DisallowWordService disallow_wordService;

    /**
     * 增加敏感词
     */
    @PostMapping("/add")
    @ResponseBody
    public CommonResp<DisallowWord> add(DisallowWord disallow_word) {
        CommonResp<DisallowWord> commonResp = new CommonResp<>();
        disallow_wordService.add(disallow_word.getValue(), commonResp);
        return commonResp;
    }

    /**
     * 删除敏感词
     */
    @PostMapping("/delete")
    @ResponseBody
    public CommonResp<DisallowWord> delete(DisallowWord disallow_word) {
        CommonResp<DisallowWord> commonResp = new CommonResp<>();
        disallow_wordService.delete(disallow_word.getValue(), commonResp);
        return commonResp;
    }
}
