package org.nuist.myblog.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Value("${kafka.topics}")
    private String topic;

    @Bean
    public NewTopic topic(){
        return TopicBuilder.name(topic).build();
    }

    // 无用
    @Bean
    public NewTopic myTopic() {
        return new NewTopic("my-topic", 1, (short) 1);
    }

}