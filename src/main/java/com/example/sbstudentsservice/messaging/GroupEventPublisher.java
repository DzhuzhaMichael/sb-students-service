package com.example.sbstudentsservice.messaging;

import com.example.sbstudentsservice.dto.message.GroupCreatedEvent;
import com.example.sbstudentsservice.model.Group;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class GroupEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    private final String exchange;

    private final String routingKey;

    public GroupEventPublisher(RabbitTemplate rabbitTemplate,
                               @Value("${rabbitmq.exchange}") String exchange,
                               @Value("${rabbitmq.routing.key}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void publishGroupCreatedEvent(Group group) {
        GroupCreatedEvent event = new GroupCreatedEvent(
                group.getId(), group.getName(), group.getCurator(), LocalDateTime.now()
        );
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        log.info("⬆ Published event: {}", event);
    }
}
