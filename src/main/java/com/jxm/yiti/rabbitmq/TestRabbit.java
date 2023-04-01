package com.jxm.yiti.rabbitmq;

public class TestRabbit {
    public static void main(String[] args) throws Exception{
        ReceiverRabbit receiver = new ReceiverRabbit("testQueue");
        Thread receiverThread = new Thread(receiver);
        receiverThread.start();

        SenderRabbit sender = new SenderRabbit("testQueue");
        for (int i = 0; i < 5; ++i) {
            MessageInfoRabbit messageInfo = new MessageInfoRabbit();
            messageInfo.setChannel("test");
            messageInfo.setContent("msg" + i);
            sender.sendMessage(messageInfo);
        }
    }
}
