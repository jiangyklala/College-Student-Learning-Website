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
            value = "gptDailyRecord.enabled",
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
            String timesKey = "yt:gpt:times:" + yesterday;
            String timesTotalKey = "yt:gpt:times:total";
            String tokensKey = "yt:gpt:tokens:" + yesterday;
            String tokensTotalKey = "yt:gpt:tokens:total";

            if (jedis.exists(timesKey)) {
                String times = jedis.get(timesKey);
                chatRecordInfo.setTimes(times);

                // 自增总 time 记录
                jedis.incrBy(timesTotalKey, Long.parseLong(times));

                // 删除昨日记录
                jedis.expire(timesKey, 0);
            }

            if (jedis.exists(tokensKey)) {
                String tokens = jedis.get(tokensKey);
                chatRecordInfo.setTokens(tokens);

                // 自增总 token 记录
                jedis.incrBy(tokensTotalKey, Long.parseLong(tokens));

                // 删除昨日记录
                jedis.expire(tokensKey, 0);
            }

            try {
                chatRecordInfo.setDate(yesterday);
                chatRecordInfoMapper.insert(chatRecordInfo);
            } catch (RuntimeException e) {
                LOG.error("更新记录失败", e);
            }
        }
    }

}
