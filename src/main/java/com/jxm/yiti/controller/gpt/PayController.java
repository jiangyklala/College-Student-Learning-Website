package com.jxm.yiti.controller.gpt;

import com.jxm.yiti.req.CheckPayReq;
import com.jxm.yiti.req.PayReq;
import com.jxm.yiti.req.VipPayWithReq;
import com.jxm.yiti.req.VipToUserQeq;
import com.jxm.yiti.resp.CheckPayResp;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.VipPayWithResp;
import com.jxm.yiti.service.PayService;
import jakarta.annotation.Resource;
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
    public CommonResp<VipPayWithResp> vipPayWith(@RequestBody VipPayWithReq vipPayWithReq) {
        CommonResp<VipPayWithResp> resp = new CommonResp<>();
        payService.vipPayWith(vipPayWithReq, resp);
        return resp;
    }

    /**
     * 设置用户类型
     *
     */
    @PostMapping("/vipToUser")
    @ResponseBody
    public CommonResp vipToUser(@RequestBody VipToUserQeq vipToUserQeq) {
        CommonResp resp = new CommonResp();
        log.info(vipToUserQeq.toString());
        payService.vipToUsers(vipToUserQeq.getUserId(), vipToUserQeq.getNum(), resp);
        return resp;
    }

    /**
     * 生成订单
     */
    @PostMapping("/checkIfPay")
    @ResponseBody
    public CommonResp<CheckPayResp> checkIfPay(@RequestBody CheckPayReq checkPayReq) {
        CommonResp<CheckPayResp> resp = new CommonResp<>();
        payService.checkIfPay(checkPayReq, resp);
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

    /**
     * 查询历史支付记录, 默认查全部
     *
     * @param from 开始时间, 格式 yyyy-MM-dd
     * @param to   结束时间, 格式 yyyy-MM-dd
     */
    @GetMapping("/payHistory")
    @ResponseBody
    public String payHistory(@RequestParam(name = "from", defaultValue = "2023-01-01") String from, @RequestParam(name = "to", defaultValue = "") String to) {

        return payService.payHistory(from, to);
    }
}
