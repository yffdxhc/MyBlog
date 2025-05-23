package org.nuist.myblog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

@Mapper
public interface Like_CollectMapper {
    Integer isBlogLike(@Param("blog_id") String blog_id, @Param("user_number") String user_number);
    Integer isBlogMarked(@Param("blog_id") String blog_id, @Param("user_number") String user_number);
    Integer like(@Param("like_collect_id") String like_collect_id, @Param("blog_id") String blog_id, @Param("user_number") String user_number, @Param("created_at")Timestamp created_at);
    Integer neverLike(@Param("blog_id") String blog_id, @Param("user_number") String user_number);
    Integer hasLiked(@Param("blog_id") String blog_id, @Param("user_number") String user_number);
}
