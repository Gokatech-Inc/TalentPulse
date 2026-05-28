package com.gokatech.talentpulse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE = "hr.events";

    public void publishEvent(String routingKey, String payload) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE, routingKey, payload);
            log.info("Published event [{}]: {}", routingKey, payload);
        } catch (Exception e) {
            log.warn("Failed to publish event [{}]: {}", routingKey, e.getMessage());
        }
    }
}
