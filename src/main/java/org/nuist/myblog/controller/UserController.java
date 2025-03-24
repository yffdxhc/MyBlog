package org.nuist.myblog.controller;

import org.nuist.myblog.entity.Result;
import org.nuist.myblog.entity.User;
import org.nuist.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getUserByUsername")
    public Result<User> getUserByUsername(String username) {
        Result<User> result = new Result<>(true, "查询成功", userService.getUserByUsername(username));
        return result;
    }
}
