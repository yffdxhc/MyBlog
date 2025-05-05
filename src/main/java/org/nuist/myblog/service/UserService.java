package org.nuist.myblog.service;

import org.nuist.myblog.entity.Result;
import org.nuist.myblog.entity.ToEmail;
import org.nuist.myblog.entity.User;
import org.nuist.myblog.util.KafkaProducer;
import org.nuist.myblog.mapper.UserMapper;
import org.nuist.myblog.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Value("${spring.mail.username}")
    private String from; // 从配置自动注入发件邮箱

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private JavaMailSender mailSender;

    private static final String USER_CACHE_KEY = "user:%s";

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    public User getUserByUsername(String username) {
        // 构建缓存键
        String cacheKey = String.format(USER_CACHE_KEY, username);

        // 从Redis中获取用户信息
        User user = (User) redisUtil.get(cacheKey);
        if (user != null) {
            return user;
        }

        // 如果缓存中没有，从数据库中查询
        user = userMapper.getUserByUsername(username);
        if (user != null) {
            // 发送消息到 Kafka
            try {
                kafkaProducer.sendUser(user);
            }catch (Exception e){
                log.error("发送消息到Kafka失败", e);
            }
        }
        return user;
    }

    /**
     * 邮箱测试
     * @param toEmail
     * @return
     */
    public Result<String[]> commonEmail(ToEmail toEmail) {
        // 原始代码中这些地方需要检查：
        // 确保 @Value 注入成功
        if (from == null){
            toEmail.getTos();// 确保参数不为空
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // 需要确保 from 字段已通过 @Value 注入
        message.setTo(toEmail.getTos());
        message.setSubject(toEmail.getSubject());
        message.setText(toEmail.getContent());

        try {
            mailSender.send(message);
            return new Result<>(true, "发送普通邮件成功", toEmail.getTos());
        } catch (MailException e) {
            log.error("邮件发送失败", e); // 推荐使用日志记录代替 e.printStackTrace()
            return new Result<>(false, "邮件发送失败: " + e.getMessage(), null);
        }
    }

    /**
     * 用户id、密码登录
     */
    public User loginId(User user) {
        log.info("进行服务：用户登录，user_id:{},password:{}", user.getUser_id(), user.getPassword());
        User loginUser = userMapper.loginId(user);
        log.info("查询用户消息，user_id:{}", loginUser.getUser_id());
        return loginUser;
    }

    public List<User> getUserByQuery(String query) {
        log.info("进行服务：查询用户，query:{}", query);
        List<User> users = userMapper.getUserByQuery(query);
        log.info("查询用户消息，query:{}", users);
        return users;
    }
}
