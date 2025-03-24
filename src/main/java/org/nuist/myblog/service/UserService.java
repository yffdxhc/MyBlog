package org.nuist.myblog.service;

import org.nuist.myblog.entity.User;
import org.nuist.myblog.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }
}
