package com.jxm.yiti.utils;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@Slf4j
public class MyCacheConfig {

    private static final Integer MAX_SIZE = 10000;

    @Bean("myKeyGenerator")
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            log.info(method.getName() + "[" + Arrays.asList(params).toString() + "]");
            return method.getName() + "[" + Arrays.asList(params).toString() + "]";
        };
    }

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        //Caffeine配置
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                //最后一次写入后经过固定时间过期
                .expireAfterWrite(1, TimeUnit.HOURS)
                //缓存的最大条数
                .maximumSize(MAX_SIZE);
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }
}
