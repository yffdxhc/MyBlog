package org.nuist.myblog.util;

import org.nuist.myblog.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String,User> kafkaTemplate;

    @Value("${kafka.topics}")
    private String topic;


    public void sendUser(User user){
        kafkaTemplate.send(topic, user);
    }

    public void sendMessage(User user) {
        kafkaTemplate.send(topic, user);
    }
}