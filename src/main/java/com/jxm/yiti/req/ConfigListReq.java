package com.jxm.yiti.req;

import java.util.List;

import lombok.Data;

/**
 * @author jiangyunkai <jiangyunkai@kuaishou.com>
 * Created on 2024-04-18
 */
@Data
public class ConfigListReq {

    private String prefix;

    private List<String> name;
}
