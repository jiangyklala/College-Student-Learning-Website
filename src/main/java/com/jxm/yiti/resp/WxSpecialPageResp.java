package com.jxm.yiti.resp;

import lombok.Data;

/**
 * @author jiangyunkai <jiangyunkai@kuaishou.com>
 * Created on 2024-02-18
 */
@Data
public class WxSpecialPageResp extends PageResp {
    private Integer id;
    private String title;
    private String answer;

    private DynamicConfigResp dynamicConfigResp;
}
