package com.gokatech.talentpulse.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange hrEventsExchange() { return new TopicExchange("hr.events"); }

    @Bean
    public TopicExchange hrAlertsExchange() { return new TopicExchange("hr.alerts"); }

    @Bean
    public Queue onboardingQueue() { return new Queue("queue.onboarding", true); }

    @Bean
    public Queue attritionAlertQueue() { return new Queue("queue.attrition.alerts", true); }

    @Bean
    public Binding onboardingBinding() { return BindingBuilder.bind(onboardingQueue()).to(hrEventsExchange()).with("employee.onboarded"); }

    @Bean
    public Binding attritionBinding() { return BindingBuilder.bind(attritionAlertQueue()).to(hrEventsExchange()).with("attrition.risk.#"); }
}
