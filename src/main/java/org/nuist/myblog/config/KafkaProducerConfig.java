package org.nuist.myblog.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.nuist.myblog.entity.User;
import org.omg.CORBA.UserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    // kafka服务器地址
    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;

    // 配置
    public Map<String, Object> config(){
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);// kafka服务器地址
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // 键序列化
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); // 值序列化
        return config;
    }

    // factory类，用于发送消息
    @Bean
    public ProducerFactory<String, User> producerFactory(){
        return new DefaultKafkaProducerFactory<>(config());
    }

    @Bean
    public KafkaTemplate<String,User> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }
}
