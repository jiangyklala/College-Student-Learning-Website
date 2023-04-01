package com.jxm.yiti.rabbitmq;

import com.alibaba.fastjson2.JSON;
import com.jxm.yiti.websocket.WebSocketServer;
import com.rabbitmq.client.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class ReceiverRabbit extends BaseConnector implements Runnable, Consumer {

    private static WebSocketServer webSocketServer;

    public WebSocketServer getWebSocketServer() {
        return webSocketServer;
    }

    @Autowired
    public void setWebSocketServer(WebSocketServer webSocketServer) {
        ReceiverRabbit.webSocketServer = webSocketServer;
    }
//    private static ReceiverRabbit receiverRabbit;

    private static final Logger LOG = LoggerFactory.getLogger(ReceiverRabbit.class);
//
//    @PostConstruct
//    public void init() {
//        receiverRabbit = this;
//        receiverRabbit.webSocketServer = this.webSocketServer;
//    }

    public ReceiverRabbit() {
        super();
    }

    public ReceiverRabbit(String queueName) throws IOException, TimeoutException {
        super(queueName);
    }

    //当消费者注册完成自动调用
    @Override
    public void handleConsumeOk(String consumerTag) {
        LOG.info("Rabbit-Consumer registered: "+ consumerTag);
    }

    //当消费者接收到消息会自动调用
    @Override
    public void handleDelivery(String consumerTag, Envelope env,
                               AMQP.BasicProperties props, byte[] body) throws IOException {
        MessageInfoRabbit messageInfoRabbit = JSON.parseObject(body, MessageInfoRabbit.class);
        LOG.info("Rabbit-Consumer receive messages: [ channel: {}, content: {} ]", messageInfoRabbit.getChannel(), messageInfoRabbit.getContent());
        webSocketServer.sendInfo(messageInfoRabbit.getContent());
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
