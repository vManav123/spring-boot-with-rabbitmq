package com.vmanav123.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueueSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    @Autowired
    private Binding binding;

    public String sendToQueue(String order) {
        try {
            rabbitTemplate.convertAndSend(this.queue.getName(), order);
        } catch (Exception e) {
            return "Failed";
        }
        return "Success";
    }

    public String sendToExchange(String textMessage) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("ENV", "LOCAL");
        Message message = new Message(textMessage.getBytes(), messageProperties);
        try {
            rabbitTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), message);
        } catch (Exception e) {
            return "Failed";
        }
        return "Success";
    }
}