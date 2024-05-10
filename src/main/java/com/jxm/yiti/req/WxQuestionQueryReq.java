package com.jxm.yiti.req;

import lombok.Data;

import java.util.List;

@Data
public class WxQuestionQueryReq extends PageReq {
    private String title;

    private Integer categoryId;

    private List<Integer> categoryIdList;

    private Integer like;

    private Integer collect;

    private Integer points;

    private Integer importanceLevel;

    private Long answerId;
}