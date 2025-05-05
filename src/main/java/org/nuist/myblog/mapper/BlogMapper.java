package org.nuist.myblog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nuist.myblog.entity.Blog;

import java.util.List;

@Mapper
public interface BlogMapper {
    List<Blog> getBlogs();
    Blog getBlogById(String blog_id);
    List<Blog> getBlogsSearched(@Param("query") String query);
}
