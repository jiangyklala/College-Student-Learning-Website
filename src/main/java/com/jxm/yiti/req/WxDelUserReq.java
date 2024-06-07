package com.jxm.yiti.req;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author jiangyk
 * Created on 2024-06-07
 */
@Setter
@Getter
@Accessors(chain = true)
public class WxDelUserReq {
    /**
     * 用户昵称
     */
    private String userName;
}
