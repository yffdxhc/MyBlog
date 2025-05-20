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
    Integer insertBlog(@Param("blog") Blog blog);
    List<Blog> getBlogsByUserNumber(@Param("user_number") String user_number);
    /**
     * 根据多个博客 ID 查询博客详情
     */
    List<Blog> findBlogsByIds(@Param("ids") List<String> ids);

    /**
     * 查询所有用户编号（用于构建 Mahout 映射）
     */
    List<String> findAllUserNumbers();

    /**
     * 查询所有博客 ID（用于构建 Mahout 映射）
     */
    List<String> findAllBlogIds();
}
