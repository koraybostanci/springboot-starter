package dev.coding.springboot.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.amqp.core.BindingBuilder.bind;
import static org.springframework.amqp.core.ExchangeBuilder.directExchange;
import static org.springframework.amqp.core.QueueBuilder.durable;

@Configuration
public class AmqpConfiguration {

    private final RabbitMQProperties rabbitMqProperties;

    public AmqpConfiguration(final RabbitMQProperties rabbitMqProperties) {
        this.rabbitMqProperties = rabbitMqProperties;
    }

    @Bean
    public Exchange exchangeTasks() {
        return directExchange(rabbitMqProperties.getExchangeName()).build();
    }

    @Bean
    public Queue queueTasksReceived() {
        return durable(rabbitMqProperties.getTasksReceived().getQueueName())
                .deadLetterExchange(rabbitMqProperties.getExchangeName())
                .deadLetterRoutingKey(rabbitMqProperties.getDeadLetter().getRoutingKey())
                .build();
    }

    @Bean
    public Queue queueDeadLetter() {
        return durable(rabbitMqProperties.getDeadLetter().getQueueName()).build();
    }

    @Bean
    public Binding bindingTasksReceived() {
        return bind(queueTasksReceived())
                .to(exchangeTasks())
                .with(rabbitMqProperties.getTasksReceived().getRoutingKey())
                .noargs();
    }

    @Bean
    public Binding bindingDeadLetter() {
        return bind(queueDeadLetter())
                .to(exchangeTasks())
                .with(rabbitMqProperties.getDeadLetter().getRoutingKey())
                .noargs();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

}
