package com.elcom.follow.messaging.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 *
 * @author Admin
 */
@Configuration
public class RpcConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcConfig.class);
    
    @Value("${learn.follow.rpc.exchange}${tech.env}")
    private String exchange;
    @Value("${learn.follow.rpc.queue}${tech.env}")
    private String queue;
    @Value("${learn.follow.rpc.key}${tech.env}")
    private String key;

    @Bean("rpcFollowQueue")
    @Primary
    public Queue rpcQueue() {
        LOGGER.info("rpcQueueName: {}", queue);
        return new Queue(queue);
    }

    @Bean("rpcFollowExchange")
    public DirectExchange rpcExchange() {
        LOGGER.info("rpcExchangeName: {}", exchange);
        return new DirectExchange(exchange);
    }

    @Bean("rpcFollowBinding")
    public Binding binding(DirectExchange rpcExchange, Queue rpcQueue) {
        LOGGER.info("rpcRoutingKey: {}", key);
        return BindingBuilder.bind(rpcQueue).to(rpcExchange).with(key);
    }

    @Bean
    public RpcServer rpcServer() {
        return new RpcServer();
    }
}
