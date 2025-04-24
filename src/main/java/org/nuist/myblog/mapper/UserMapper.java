package org.nuist.myblog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nuist.myblog.entity.User;

@Mapper
public interface UserMapper {
    User getUserByUsername(String username);
    User loginId(@Param("user") User user);
    User getUserByUserNumber(String user_number);
}
