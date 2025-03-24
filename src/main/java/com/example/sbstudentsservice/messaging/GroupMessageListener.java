package com.example.sbstudentsservice.messaging;

import com.example.sbstudentsservice.dto.message.GroupCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class GroupMessageListener {

    private final Set<Long> processedMessages = Collections.synchronizedSet(new HashSet<>());

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void receiveMessage(GroupCreatedEvent event) {
        if (processedMessages.contains(event.getId())) {
            log.warn("⚠ Message with id {} was already processed. Skipping. ", event.getId());
            return;
        }

        log.info("✉ Received message: {}", event);
        processedMessages.add(event.getId());
    }

}
