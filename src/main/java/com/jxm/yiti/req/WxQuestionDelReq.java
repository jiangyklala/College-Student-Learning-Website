package com.jxm.yiti.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxQuestionDelReq {
    Integer wxQuestionId;

    Long wxQuestionAnswerId;
}
