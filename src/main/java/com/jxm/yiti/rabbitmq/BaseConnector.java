package com.jxm.yiti.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class BaseConnector {
    protected Channel channel;
    protected Connection connection;
    protected String queueName;

    public BaseConnector() {}

    public BaseConnector(String queueName) throws IOException, TimeoutException {
        this.queueName = queueName;

        ConnectionFactory factory = new ConnectionFactory();          //打开连接和创建频道
        factory.setHost("124.223.184.187");                           //设置 RabbitMQ 所在主机 ip, user, password
        factory.setUsername("admin");
        factory.setPassword("jiang");
        connection = factory.newConnection();                         //创建连接
        channel = connection.createChannel();                         //创建频道
        channel.queueDeclare(queueName, false, false, false, null);   //声明创建队列
    }
}