package com.jxm.yiti.rabbitmq;

import com.alibaba.fastjson2.JSON;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiverRabbit extends BaseConnector implements Runnable, Consumer {

    private static final Logger LOG = LoggerFactory.getLogger(ReceiverRabbit.class);

    public ReceiverRabbit(String queueName) throws IOException, TimeoutException {
        super(queueName);
    }

    //当消费者注册完成自动调用
    @Override
    public void handleConsumeOk(String consumerTag) {
        System.out.println("Consumer "+consumerTag +" registered");
    }

    //当消费者接收到消息会自动调用
    @Override
    public void handleDelivery(String consumerTag, Envelope env,
                               AMQP.BasicProperties props, byte[] body) throws IOException {
        MessageInfoRabbit messageInfoRabbit = JSON.parseObject(body, MessageInfoRabbit.class);
        LOG.info("Receive message: [ channel: {}, content: {} ]", messageInfoRabbit.getChannel(), messageInfoRabbit.getContent());
    }

    @Override
    public void handleCancelOk(String consumerTag) {
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
    }

    @Override
    public void handleShutdownSignal(String consumerTag,
                                     ShutdownSignalException sig) {
    }

    @Override
    public void handleRecoverOk(String s) {

    }


    @Override
    public void run() {
        try {
            channel.basicConsume(queueName, true, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
