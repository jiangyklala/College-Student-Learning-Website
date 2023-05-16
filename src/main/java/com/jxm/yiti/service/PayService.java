package com.jxm.yiti.service;

import com.alibaba.fastjson2.JSON;
import com.jxm.yiti.domain.GptPayInfo;
import com.jxm.yiti.mapper.GptPayInfoMapper;
import com.jxm.yiti.mapper.cust.GptPayInfoMapperCust;
import com.jxm.yiti.req.VipPayWithReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.PayBusinessResp;
import com.jxm.yiti.utils.SnowFlakeIdWorker;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class PayService {

    @Resource
    BestPayService bestPayService;

    @Resource
    GptPayInfoMapper gptPayInfoMapper;

    @Resource
    GptPayInfoMapperCust gptPayInfoMapperCust;

    @Resource
    SnowFlakeIdWorker snowFlakeIdWorker;

    /**
     * 生成一个订单, 并把生成的支付 URL 写进 resp
     * @param orderId 订单号
     * @param amount 金额数
     */
    public PayResponse create(String orderId, BigDecimal amount, String orderName)  {
        // 写入数据库
        GptPayInfo gptPayInfo = new GptPayInfo();
        gptPayInfo.setOrderNo(Long.valueOf(orderId));
        gptPayInfo.setPlatformStatus(OrderStatusEnum.NOTPAY.name());
        gptPayInfo.setPayAmount(amount);
        gptPayInfo.setUserId(12312313L);
        gptPayInfoMapper.insertSelective(gptPayInfo);


        PayRequest request = new PayRequest();
        request.setOrderName(orderName);
        request.setOrderId(orderId);
        request.setOrderAmount(amount.doubleValue());
        request.setPayTypeEnum(BestPayTypeEnum.WXPAY_NATIVE);

        PayResponse response = bestPayService.pay(request);

//        log.info("response: {}", response);

        return response;
    }

    public void payAsyncNotify(String notifyData) {
        // 签名校验
        PayResponse response = bestPayService.asyncNotify(notifyData);
        log.info("response: {}", response);

        // 金额校验 (从数据库查订单)
        GptPayInfo gptPayInfo = gptPayInfoMapperCust.selectByOrderNo(Long.valueOf(response.getOrderId()));
        if (gptPayInfo == null) {
            throw new RuntimeException("通过 order_no 查询到的结果是 null");
        }

        // 修改订单支付状态
        if (!gptPayInfo.getPlatformStatus().equals(OrderStatusEnum.SUCCESS.name())) {

            if (gptPayInfo.getPayAmount().compareTo(BigDecimal.valueOf(response.getOrderAmount())) != 0) {
                throw new RuntimeException("异步通知中的金额和数据库中的不一致, or");
            }

            gptPayInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
            gptPayInfo.setPlatformNumber(response.getOutTradeNo());
            gptPayInfo.setUpdateTime(null);
            gptPayInfoMapper.updateByPrimaryKeySelective(gptPayInfo);
        }

        // 通知微信已经接受到通知了
    }

    public void vipPayWith(VipPayWithReq vipPayWithReq, CommonResp<String> resp) {
        BigDecimal amount;
        switch (vipPayWithReq.getNum()) {
            case 1:
                amount = new BigDecimal("0.01");
                break;
            case 2:
                amount = new BigDecimal("0.02");
                break;
            case 3:
                amount = new BigDecimal("0.03");
                break;
            default:
                resp.setMessage("金额选择错误");
                log.error("金额选择错误");
                return;
        }
        String orderName = "GPT 充值";
        Long orderNo = snowFlakeIdWorker.nextId();
        PayResponse payResponse = create(String.valueOf(orderNo), amount, orderName);

        resp.setContent(payResponse.getCodeUrl());
    }
}
