package com.jxm.yiti.service;

import com.jxm.yiti.domain.GptPayInfo;
import com.jxm.yiti.domain.User;
import com.jxm.yiti.mapper.GptPayInfoMapper;
import com.jxm.yiti.mapper.UserMapper;
import com.jxm.yiti.mapper.cust.GptPayInfoMapperCust;
import com.jxm.yiti.req.CheckPayReq;
import com.jxm.yiti.req.VipPayWithReq;
import com.jxm.yiti.resp.CheckPayResp;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.VipPayWithResp;
import com.jxm.yiti.utils.SnowFlakeIdWorker;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

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

    @Resource
    private UserMapper userMapper;


    /**
     * 生成一个订单, 并把生成的支付 URL 写进 resp
     *
     * @param orderId 订单号
     * @param amount  金额数
     */
    public PayResponse create(String orderId, BigDecimal amount, String orderName, Long userId) {
        // 写入数据库
        GptPayInfo gptPayInfo = new GptPayInfo();
        gptPayInfo.setOrderNo(Long.valueOf(orderId));
        gptPayInfo.setPlatformStatus(OrderStatusEnum.NOTPAY.name());
        gptPayInfo.setPayAmount(amount);
        gptPayInfo.setUserId(12312313L);
        gptPayInfo.setUserId(userId);
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

    public void vipPayWith(VipPayWithReq vipPayWithReq, CommonResp<VipPayWithResp> resp) {
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
        PayResponse payResponse = create(String.valueOf(orderNo), amount, orderName, vipPayWithReq.getUserId());

        VipPayWithResp vipPayWithResp = new VipPayWithResp();
        vipPayWithResp.setCodeUrl(payResponse.getCodeUrl());
        vipPayWithResp.setOrderId(String.valueOf(orderNo));
        resp.setContent(vipPayWithResp);
    }

    public void checkIfPay(CheckPayReq checkPayReq, CommonResp<CheckPayResp> resp) {
        CheckPayResp checkPayResp = new CheckPayResp();
        log.info("orderId : {}", checkPayReq.getOrderId());
        try {
            GptPayInfo gptPayInfo = gptPayInfoMapperCust.selectByOrderNo(Long.valueOf(checkPayReq.getOrderId()));

            if (gptPayInfo.getPlatformStatus().equals("SUCCESS")) {
                checkPayResp.setSuccess(true);
            }
        } catch (RuntimeException e) {
            log.error("查询是否支付失败, orderId: {}", checkPayReq.getOrderId(), e);
        }

        resp.setContent(checkPayResp);
    }

    public void vipToUsers(Long userId, Integer num, CommonResp resp) {
        int days = 0;
        switch (num) {
            case 1:
                days = 31;
                break;
            case 2:
                days = 93;
                break;
            case 3:
                days = 366;
                break;
            default:
                resp.setMessage("参数错误");
                log.error("天数选择错误, userId: {}, chooseValue: {}", userId, num);
                return;
        }

        User user = userMapper.selectByPrimaryKey(userId);

        if (user == null) {
            resp.setSuccess(false);
            resp.setMessage("用户不存在!");
            return;
        }

        Integer oldType = user.getType();
        if (oldType == 2) {
            resp.setSuccess(false);
            resp.setMessage("已经是会员嘞");
            return;
        }

        user.setType(2);

        try {
            userMapper.updateByPrimaryKey(user);

            // 添加 Redis
            String vipKey = "yt:vip:" + user.getEmail();
            try (Jedis jedis = UserService.jedisPool.getResource()) {
                jedis.setex(vipKey, 60L * 60 * 24 * days, "");
            }
        } catch (Exception e) {
            resp.setSuccess(false);
            resp.setMessage("用户类型修改出错");
            log.error("用户类型修改出错, userId: {}, chooseValue: {}", userId, num);
            return;
        }

        resp.setMessage("充值成功!");
    }
}
