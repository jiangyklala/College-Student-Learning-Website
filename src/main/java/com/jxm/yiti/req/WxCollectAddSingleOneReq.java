package com.jxm.yiti.req;

import lombok.Data;

/**
 * 为某个用户的 collect set 中添加题目 id
 */
@Data
public class WxCollectAddSingleOneReq {
    private Long answerId;
}