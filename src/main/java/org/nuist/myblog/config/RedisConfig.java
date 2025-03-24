package org.nuist.myblog.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching  //支持缓存
public class RedisConfig extends CachingConfigurerSupport {
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){
    RedisTemplate<String,Object> template = new RedisTemplate<>();
    //配置连接工厂
        template.setConnectionFactory(factory);
        //使用Jackson2JsonRedisSerializer 来进行序列化和反序列化redis的value值
        //默认使用jdk提供给我们的序列化方式
        Jackson2JsonRedisSerializer jacksonSeial =
                new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        //指定序列化的域 filed get set 以及修饰符的范围ANY表示都有包括public private
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型 类必须是非final修饰的 被final修饰的类 在运行时会出现异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);

        //value使用json格式进行序列化
        template.setValueSerializer(jacksonSeial);
        //使用StringRedisSerializer来进行序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());

        //设置hash key 和value的序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonSeial);
        template.afterPropertiesSet();

        return template;


    }

//    针对不同类型的redis数据的操作进行封装

//对hash类型的数据进行操作
    @Bean
    public HashOperations<String,String,Object> hashOperations
    (RedisTemplate<String,Object> redisTemplate){
        return redisTemplate.opsForHash();
    }
//    对redis中String类型数据操作的封装
    @Bean
    public ValueOperations<String,Object> valueOperations
    (RedisTemplate<String,Object> redisTemplate){
        return redisTemplate.opsForValue();
    }

    //对链表类型的数据操作
    @Bean
    public ListOperations<String,Object> listOperations
    (RedisTemplate<String,Object> redisTemplate){
        return redisTemplate.opsForList();
    }
    //对无序集合类型的数据操作
    @Bean
    public SetOperations<String,Object> setOperations
    (RedisTemplate<String,Object> redisTemplate){
        return redisTemplate.opsForSet();
    }
//    对有序结合类型的数据操作
    @Bean
    public ZSetOperations<String,Object> zSetOperations
    (RedisTemplate<String,Object> redisTemplate){
        return redisTemplate.opsForZSet();
    }


}


