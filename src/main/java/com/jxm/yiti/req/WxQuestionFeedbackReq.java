package com.jxm.yiti.req;

import lombok.Data;

@Data
public class WxQuestionFeedbackReq {
    private String questionTitle;
    private String feedbackContent;
    private String userEmail;
}
