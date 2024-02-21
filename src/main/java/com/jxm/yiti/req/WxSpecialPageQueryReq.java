package com.jxm.yiti.req;

import lombok.Data;

/**
 * @author jiangyunkai <jiangyunkai@kuaishou.com>
 * Created on 2024-02-18
 */
@Data
public class WxSpecialPageQueryReq extends PageReq {
    private Integer id;
    private String title;
}
