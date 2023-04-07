package com.jxm.yiti.rabbitmq;

import com.alibaba.fastjson2.JSON;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
public class SenderRabbit extends BaseConnector {

    SenderRabbit() {}

    public SenderRabbit(String queueName) throws IOException, TimeoutException {
        super(queueName);
    }

    public void sendString(String message) throws IOException {
        channel.basicPublish("",queueName, null, message.getBytes(StandardCharsets.UTF_8));
    }

    public void sendMessage(Serializable object) throws IOException {
        channel.basicPublish("",queueName, null, JSON.toJSONBytes(object));
    }
}
