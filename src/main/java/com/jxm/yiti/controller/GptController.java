package com.jxm.yiti.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.service.GptService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gpt")
public class GptController {

    @PostMapping("/getData")
    @ResponseBody
    public String get(@RequestBody JSONObject jsonObject){
        String res = GptService.sendPost(jsonObject.getString("data"));
        System.out.println(jsonObject.toJSONString());
        return JSON.toJSONString(res);
    }

    @PostMapping("/getData2/{data}")
    @ResponseBody
    public CommonResp<String> getData2(@PathVariable String data){
        CommonResp<String> resp = new CommonResp<>();
        String res = GptService.sendPost(data);
        resp.setContent(res);
        return resp;
    }

}
