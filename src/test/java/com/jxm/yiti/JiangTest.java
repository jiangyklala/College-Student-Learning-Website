package com.jxm.yiti;

import org.junit.Test;

import com.jxm.yiti.config.RedisKeyAliasName;
import com.jxm.yiti.resp.CommonResp2;
import com.jxm.yiti.utils.JSONUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyunkai <jiangyunkai@kuaishou.com>
 * Created on 2024-04-21
 */
@Slf4j
public class JiangTest {

    @Test
    public void test() {
        String s = JSONUtils.addConfig(
                new CommonResp2(),
                RedisKeyAliasName.WXAPP_VIP_PRICE.getKeyName(),
                RedisKeyAliasName.WXAPP_PAY_PAGE_SHOW.getKeyName());
        log.info("res: {}", s);
    }
}
