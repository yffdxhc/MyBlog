package org.nuist.myblog.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FollowMapper {
    List<String> getUserFollows(String user_number);
}
