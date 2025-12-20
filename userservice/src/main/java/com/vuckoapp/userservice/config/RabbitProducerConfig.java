package com.vuckoapp.userservice.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
public class RabbitProducerConfig {

    public static final String EXCHANGE = "notification-exchange";
    public static final String ROUTING_KEY = "notification.send";

    @Bean
    public Jackson2JsonMessageConverter jacksonJsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(jacksonJsonMessageConverter());
        return template;
    }
}
