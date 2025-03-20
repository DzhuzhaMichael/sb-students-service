package com.example.sbstudentsservice.messaging;

import com.example.sbstudentsservice.dto.message.GroupCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class GroupMessageListener {

    private final Set<Long> processedMessages = Collections.synchronizedSet(new HashSet<>());

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void receiveMessage(GroupCreatedEvent event) {
        if (processedMessages.contains(event.getId())) {
            System.out.println("⚠ Message with id " + event.getId() + " was processed. Skipped.");
            return;
        }

        System.out.println("✉ Received message: " + event);
        processedMessages.add(event.getId());
    }

}
