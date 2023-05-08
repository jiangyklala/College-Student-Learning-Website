package com.jxm.yiti.controller;

import com.jxm.yiti.domain.MailObject;
import com.jxm.yiti.req.PayReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.service.PayService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/pay")
public class PayController {

    @Resource
    PayService payService;

    /**
     * 支付接口
     */
    @PostMapping("/create")
    @ResponseBody
    public CommonResp send(@RequestBody PayReq payReq) {
        CommonResp resp = new CommonResp();
        payService.create(payReq.getOrderId(), payReq.getAmount());
        return resp;
    }

    /**
     * 支付接口
     */
    @PostMapping("/notify")
    @ResponseBody
    public CommonResp notify(@RequestBody String notifyData) {
        CommonResp resp = new CommonResp();
        log.info("data: {}", notifyData);
        return resp;
    }
}
