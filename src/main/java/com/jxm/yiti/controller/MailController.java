package com.jxm.yiti.controller;

import com.jxm.yiti.domain.MailObject;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.service.MailService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/mail")
public class MailController {

    @Resource
    private MailService mailService;

    /**
     * 注册接口
     */
    @PostMapping("/send")
    @ResponseBody
    public CommonResp send(@RequestBody MailObject mailObject) throws MessagingException, UnsupportedEncodingException {
        CommonResp resp = new CommonResp();
        resp.setSuccess(mailService.simpleSend(mailObject));
        return resp;
    }

}
