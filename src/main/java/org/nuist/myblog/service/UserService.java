package org.nuist.myblog.service;

import org.nuist.myblog.entity.Blog;
import org.nuist.myblog.entity.Result;
import org.nuist.myblog.entity.ToEmail;
import org.nuist.myblog.entity.User;
import org.nuist.myblog.mapper.BlogMapper;
import org.nuist.myblog.mapper.FollowMapper;
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

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Value("${spring.mail.username}")
    private String from; // 从配置自动注入发件邮箱

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private BlogMapper  blogMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private JavaMailSender mailSender;

    private static final String USER_CACHE_KEY = "user:%s";

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

    public User getUserByUserNumber(String user_number) {
        log.info("进行服务：查询用户，user_number:{}", user_number);
        User user = userMapper.getUserByUserNumber(user_number);
        log.info("查询用户消息，user_number:{}", user);
        return user;
    }

    public List<User> getUserFollows(String user_number) {
        log.info("进行服务：查询用户关注，user_number:{}", user_number);
        List<User> follows = new ArrayList<>();
        List<String> follows_number = followMapper.getUserFollows(user_number);
        for (String follows_number_item : follows_number){
            log.info("进行服务：查询用户关注，follows_number_item:{}", follows_number_item);
            User user = userMapper.getUserByUserNumber(follows_number_item);
            user.setPassword("************************");
            log.info("查询用户消息，follows_number_item:{}", user);
            follows.add(user);
        }
        return follows;
    }

    public List<Blog> getBlogsByUserNumber(String user_number) {
        log.info("进行服务：查询用户博客，user_number:{}", user_number);
        List<Blog> blogs = blogMapper.getBlogsByUserNumber(user_number);
        for (Blog blog : blogs){
            log.info("进行服务：查询用户博客，blog:{}", blog);
            User user = userMapper.getUserByUserNumber(blog.getUser_number());
            blog.setUsername(user.getUsername());
            blog.setUser_avatar(user.getAvatar());
        }
        return blogs;
    }
}
