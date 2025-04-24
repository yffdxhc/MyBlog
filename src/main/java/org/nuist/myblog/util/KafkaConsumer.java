package org.nuist.myblog.util;

import org.nuist.myblog.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
    @Autowired
    private RedisUtil redisUtil;

    @KafkaListener(topics = "${kafka.topics}", groupId = "my-group",containerFactory = "concurrentKafkaListenerContainerFactory")
    void listen(User user) {
        log.info("Received User in group my-group: " + user.toString());
        Map<String, Object> map = BeanUtil.getMapFromBean(user);
        redisUtil.hmset("user:"+user.getUser_id(),map);
    }
}
