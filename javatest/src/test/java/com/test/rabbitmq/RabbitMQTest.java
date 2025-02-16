package com.test.rabbitmq;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMQTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage() {
        rabbitTemplate.convertAndSend("myQueue", "Hello, World3!");
    }
    @Test
    void testWorkQueue() throws InterruptedException {
        String queueName = "work.queue";
        for (int i = 0; i < 50; i++) {
            String message = "Message " + i;
            rabbitTemplate.convertAndSend(queueName,message);
            Thread.sleep(20);
        }
    }
}
