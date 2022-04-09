package com.vmanav123.rabbitmq.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vmanav123.rabbitmq.config.QueueSender;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testing")
public class MessageController {

    @Autowired
    private QueueSender queueSender;

    @Autowired
    private RabbitTemplate template;

    @GetMapping(path = "/sendToQueue")
    public String send(@RequestParam("message") String message){
        return "Send result : "+queueSender.sendToQueue("test message : "+message);
    }

    @GetMapping(path = "/sendToExchange")
    public String convertAndSend(@RequestParam("message") String textMessage) throws JsonProcessingException {
        return "Send result : "+queueSender.sendToExchange(textMessage);
    }

}