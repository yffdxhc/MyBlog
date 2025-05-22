package org.nuist.myblog.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FollowMapper {
    // 获取用户关注列表
    List<String> getUserFollows(String user_number);
    // 获取用户粉丝列表
    List<String> getUserFollowers(String user_number);
}
