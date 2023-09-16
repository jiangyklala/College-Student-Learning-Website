//package com.jxm.yiti.rabbitmq;
//
//import java.io.Serial;
//import java.io.Serializable;
//
//public class MessageInfoRabbit implements Serializable {
//    @Serial
//    private static final long serialVersionUID = 1L;
//
//    //渠道
//    private String channel;
//
//    //来源
//    private String content;
//
//    public MessageInfoRabbit() { }
//
//    public MessageInfoRabbit(String channel, String content) {
//        this.channel = channel;
//        this.content = content;
//    }
//
//    public String getChannel() {
//        return channel;
//    }
//
//    public void setChannel(String channel) {
//        this.channel = channel;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//}