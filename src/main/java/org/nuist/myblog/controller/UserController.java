package org.nuist.myblog.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.nuist.myblog.entity.Blog;
import org.nuist.myblog.entity.Result;
import org.nuist.myblog.entity.ToEmail;
import org.nuist.myblog.entity.User;
import org.nuist.myblog.service.UserService;
import org.nuist.myblog.util.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    /**
     * 用户登录1：用户id、密码登录
     *
     * @return
     */
    @PostMapping("/loginId")
    public Result<String> login(User user) {
        log.info("收到请求：用户登录，user_id:{},password:{}", user.getUser_id(), user.getPassword());
        Result<String> result = new Result<>();
        try {
            User loginUser = userService.loginId(user);
            Map<String, String> payload = new HashMap<>();
            payload.put("user_number", loginUser.getUser_number());
            // 生成jwt令牌
            String token = JWTUtils.getToken(payload);
            result.setData(token);
            result.setSuccess(true);
            result.setMessage("认证成功");
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 邮箱测试
     *
     * @param tos
     * @return
     */
    @PostMapping("/commonEmail")
    public Result<String[]> commonEmail(String tos) {
        ToEmail toEmail = new ToEmail(tos.split(","), "测试邮件", "测试邮件内容");
        log.info("收到请求：发送邮件，toEmail:{}", toEmail);
        Result<String[]> result = userService.commonEmail(toEmail);
        return result;
    }

    @PostMapping("/test")
    public Result<String> test(HttpServletRequest request) {
        Result<String> result = new Result<>();
        // Map<String, Object> map = new HashMap<>();
        // 验证令牌  交给拦截器去做
        // 只需要在这里处理自己的业务逻辑
        String token = request.getHeader("token");
        DecodedJWT verify = JWTUtils.verify(token);
        log.info("用户编号：[{}]",verify.getClaim("user_number").asString());
        result.setData(verify.getClaim("user_number").asString());
        result.setSuccess(true);
        result.setMessage("token正常");
        // map.put("state", true);
        // map.put("msg", "请求成功");
        return result;
    }

    @GetMapping("/getUserSearched")
    public Result<List<User>> getUserSearched(@RequestParam("query") String query) {
        log.info("收到请求：查询用户信息，query:{}", query);
        Result<List<User>> result = new Result<>(true, "查询成功", userService.getUserByQuery(query));
        return result;
    }

    @GetMapping("/getUserFollows")
    public Result<List<User>> getUserFollows(HttpServletRequest request) {
        String token = request.getHeader("token");
        DecodedJWT verify = JWTUtils.verify(token);
        String user_number = verify.getClaim("user_number").asString();
        log.info("用户：[{}]申请获取关注信息",user_number);
        Result<List<User>> result = new Result<>(true, "查询关注成功", userService.getUserFollows(user_number));
        return result;
    }

}
