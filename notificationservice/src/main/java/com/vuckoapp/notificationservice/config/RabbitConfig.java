package com.vuckoapp.notificationservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String QUEUE_ACTIVATION = "activation-email-queue";

    @Bean
    public Queue activationQueue() {
        return new Queue(QUEUE_ACTIVATION, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("notification-exchange");
    }

    @Bean
    public Binding binding(Queue activationQueue, TopicExchange exchange) {
        return BindingBuilder.bind(activationQueue).to(exchange).with("activation.#");
    }

    // --- Dodajemo ovde ---
    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jacksonJsonMessageConverter());
        return template;
    }
}
