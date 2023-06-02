package com.jxm.yiti.timer;

import com.jxm.yiti.domain.ChatRecordInfo;
import com.jxm.yiti.mapper.ChatRecordInfoMapper;
import com.jxm.yiti.service.UserService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Component
public class GPTDailyRecord {

    private static final Logger LOG = LoggerFactory.getLogger(GPTDailyRecord.class);

    @Resource
    ChatRecordInfoMapper chatRecordInfoMapper;

    @ConditionalOnProperty(
            value = "gpt.daily.record.enabled",
            havingValue = "true",
            matchIfMissing = false
    )
    @Scheduled(cron = "* 1 0 * * ? ")
    public void timeAndTokenRecord() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        // 获取昨天的日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String yesterday = sdf.format(calendar.getTime());


        try (Jedis jedis = UserService.jedisPool.getResource()) {
            ChatRecordInfo chatRecordInfo = new ChatRecordInfo();
            String ntimesKey = "yt:gpt:ntimes:" + yesterday;        // 普通用户当日提问次数
            String ntimesTotalKey = "yt:gpt:ntimes:ntotal";         // 普通用户总提问次数
            String ntokensKey = "yt:gpt:ntokens:" + yesterday;      // 普通用户当日消耗 token
            String ntokensTotalKey = "yt:gpt:ntokens:ntotal";       // 普通用户总消耗 token
            String vtimesKey = "yt:gpt:vtimes:" + yesterday;        // 会员当日提问次数
            String vtimesTotalKey = "yt:gpt:vtimes:vtotal";         // 会员总提问次数
            String vtokensKey = "yt:gpt:vtokens:" + yesterday;      // 会员当日消耗 token
            String vtokensTotalKey = "yt:gpt:vtokens:vtotal";       // 会员总消耗 token
            String iVtimesKey = "yt:gpti:vtimes:" + yesterday;      // 画图当日消耗次数
            String iVtimesTotalKey = "yt:gpti:vtimes:vtotal";       // 画图总消耗次数

            // 处理普通用户
            if (jedis.exists(ntimesKey)) {
                String times = jedis.get(ntimesKey);
                chatRecordInfo.setNtimes(times);

                // 自增总 time 记录
                jedis.incrBy(ntimesTotalKey, Long.parseLong(times));

                // 删除昨日记录
                jedis.expire(ntimesKey, 0);
            }

            if (jedis.exists(ntokensKey)) {
                String tokens = jedis.get(ntokensKey);
                chatRecordInfo.setNtokens(tokens);

                // 自增总 token 记录
                jedis.incrBy(ntokensTotalKey, Long.parseLong(tokens));

                // 删除昨日记录
                jedis.expire(ntokensKey, 0);
            }

            // 处理会员
            if (jedis.exists(vtimesKey)) {
                String times = jedis.get(vtimesKey);
                chatRecordInfo.setVtimes(times);

                // 自增总 time 记录
                jedis.incrBy(vtimesTotalKey, Long.parseLong(times));

                // 删除昨日记录
                jedis.expire(vtimesKey, 0);
            }

            if (jedis.exists(vtokensKey)) {
                String tokens = jedis.get(vtokensKey);
                chatRecordInfo.setVtokens(tokens);

                // 自增总 token 记录
                jedis.incrBy(vtokensTotalKey, Long.parseLong(tokens));

                // 删除昨日记录
                jedis.expire(vtokensKey, 0);
            }

            if (jedis.exists(iVtimesKey)) {
                String iVtimes = jedis.get(iVtimesKey);
                chatRecordInfo.setIvtimes(iVtimes);

                // 自增总 token 记录
                jedis.incrBy(iVtimesTotalKey, Long.parseLong(iVtimes));

                // 删除昨日记录
                jedis.expire(iVtimesKey, 0);
            }

            try {
                chatRecordInfo.setDate(yesterday);
                chatRecordInfoMapper.insertSelective(chatRecordInfo);
            } catch (RuntimeException e) {
                LOG.error("更新记录失败", e);
            }
        }
    }

}
