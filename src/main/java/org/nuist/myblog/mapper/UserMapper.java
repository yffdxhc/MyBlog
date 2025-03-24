package org.nuist.myblog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.nuist.myblog.entity.User;

@Mapper
public interface UserMapper {
    User getUserByUsername(String username);
}
