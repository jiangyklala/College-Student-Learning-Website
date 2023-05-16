package com.jxm.yiti.controller;

import com.alibaba.fastjson2.JSON;
import com.jxm.yiti.domain.MailObject;
import com.jxm.yiti.req.PayReq;
import com.jxm.yiti.req.VipPayWithReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.PayBusinessResp;
import com.jxm.yiti.service.PayService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/pay")
public class PayController {

    @Resource
    PayService payService;

    /**
     * 生成订单
     */
    @PostMapping("/vipPayWith")
    @ResponseBody
    public CommonResp<String> vipPayWith(@RequestBody VipPayWithReq vipPayWithReq) {
        CommonResp<String> resp = new CommonResp<>();
        payService.vipPayWith(vipPayWithReq, resp);
        return resp;
    }

    /**
     * 支付接口
     */
    @PostMapping("/create")
    @ResponseBody
    public CommonResp<String> send(@RequestBody PayReq payReq) {
        CommonResp<String> resp = new CommonResp<>();
//        payService.create(payReq.getOrderId(), payReq.getAmount(), resp);
        return resp;
    }

    /**
     * 支付接口
     */
    @PostMapping("/notify")
    @ResponseBody
    public String payAsyncNotify(@RequestBody String notifyData) {

        payService.payAsyncNotify(notifyData);

        return """
                <xml>
                  <return_code><![CDATA[SUCCESS]]></return_code>
                  <return_msg><![CDATA[OK]]></return_msg>
                </xml>
                """;
    }
}
