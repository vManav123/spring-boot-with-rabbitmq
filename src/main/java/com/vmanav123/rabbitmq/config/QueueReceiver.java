package com.vmanav123.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QueueReceiver {

    @RabbitListener(queues = {"${queue.name}"})
    public void receive(@Payload String fileBody) {
        log.info("Message Received : {}",fileBody);
    }

}