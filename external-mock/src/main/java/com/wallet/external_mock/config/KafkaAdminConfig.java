package com.wallet.external_mock.config;

import java.util.HashMap;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class KafkaAdminConfig {
    
    private final KafkaProperties properties;
 
    @Bean
    KafkaAdmin kafkaAdmin(){
        var configs = new HashMap<String, Object>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    KafkaAdmin.NewTopics newTopics(){
        return new KafkaAdmin.NewTopics(
            TopicBuilder.name("cashin-topic").partitions(1).build()
            );
    }
}
