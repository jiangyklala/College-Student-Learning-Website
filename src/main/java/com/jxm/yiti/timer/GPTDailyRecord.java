package com.jxm.yiti.timer;

import com.jxm.yiti.domain.ChatRecordInfo;
import com.jxm.yiti.mapper.ChatRecordInfoMapper;
import com.jxm.yiti.service.UserService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Component
public class GPTDailyRecord {

    private static final Logger LOG = LoggerFactory.getLogger(GPTDailyRecord.class);

    @Resource
    ChatRecordInfoMapper chatRecordInfoMapper;

    @Scheduled(cron = "* 17 23 * * ? ")
    public void timeAndTokenRecord() {
        // 获取当日日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String nowTime = sdf.format(new Date());

        try (Jedis jedis = UserService.jedisPool.getResource()) {
            ChatRecordInfo chatRecordInfo = new ChatRecordInfo();
            String timesKey = "yt:gpt:times:" + nowTime;
            String timesTotalKey = "yt:gpt:times:total";
            String tokensKey = "yt:gpt:tokens:" + nowTime;
            String tokensTotalKey = "yt:gpt:tokens:total";

            if (jedis.exists(timesKey)) {
                String times = jedis.get(timesKey);
                chatRecordInfo.setTimes(times);
                jedis.incrBy(timesTotalKey, Long.parseLong(times));
                jedis.expire(timesKey, 0);
            }

            if (jedis.exists(tokensKey)) {
                String tokens = jedis.get(tokensKey);
                chatRecordInfo.setTokens(tokens);
                jedis.incrBy(tokensTotalKey, Long.parseLong(tokens));
                jedis.expire(tokensKey, 0);
            }

            try {
                chatRecordInfo.setDate(nowTime);
                chatRecordInfoMapper.insert(chatRecordInfo);
            } catch (RuntimeException e) {
                LOG.error("更新记录失败", e);
            }
        }
    }

}
