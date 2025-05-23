package org.nuist.myblog.service;

import org.nuist.myblog.entity.Blog;
import org.nuist.myblog.entity.User;
import org.nuist.myblog.mapper.BlogMapper;
import org.nuist.myblog.mapper.Like_CollectMapper;
import org.nuist.myblog.mapper.UserMapper;
import org.nuist.myblog.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class BlogService {
    private static final Logger log = LoggerFactory.getLogger(BlogService.class);

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private Like_CollectMapper like_collectMapper;

    public List<Blog> getBlogs() {
        List<Blog> blogs = blogMapper.getBlogs();
        log.info("getBlogs:{}", blogs);
        for (Blog blog : blogs){
            log.info("blog:{}", blog);
            User user = userMapper.getUserByUserNumber(blog.getUser_number());
            blog.setUsername(user.getUsername());
            blog.setUser_avatar(user.getAvatar());
        }
        return blogs;
    }

    public Blog getBlogById(String blog_id) {
        Blog blog = blogMapper.getBlogById(blog_id);
        log.info("getBlogById:{}", blog);
        User user = userMapper.getUserByUserNumber(blog.getUser_number());
        blog.setUsername(user.getUsername());
        blog.setUser_avatar(user.getAvatar());
        log.info("getBlogById:{}", blog);
        return blog;
    }

    public List<Blog> getBlogsSearched(String query) {
        List<Blog> blogs = blogMapper.getBlogsSearched(query);
        log.info("getBlogsSearched:{}", blogs);
        for (Blog blog : blogs){
            log.info("blog:{}", blog);
            User user = userMapper.getUserByUserNumber(blog.getUser_number());
            blog.setUsername(user.getUsername());
            blog.setUser_avatar(user.getAvatar());
        }
        return blogs;
    }
    public Boolean insertBlog(Blog blog) {
        Integer result = blogMapper.insertBlog(blog);
        log.info("insertBlog:{}", result);
        return result > 0;
    }

    public List<Blog> getHotBlogs(){
        List<Blog> blogs = blogMapper.getBlogs();
        for (Blog blog : blogs){
            User user = userMapper.getUserByUserNumber(blog.getUser_number());
            blog.setUsername(user.getUsername());
            blog.setUser_avatar(user.getAvatar());
        }
        return blogs;
    }

    public Boolean isBlogLike(String blog_id, String user_number){
        Integer result = like_collectMapper.isBlogLike(blog_id, user_number);
        log.info("isBlogLike:{}", result);
        return result > 0;
    }

    public Boolean isBlogCollect(String blog_id, String user_number){
        Integer result = like_collectMapper.isBlogMarked(blog_id, user_number);
        log.info("isBlogCollect:{}", result);
        return result > 0;
    }
    @Transactional
    public Boolean likeButton(String blogId, String userNumber) {
        if (like_collectMapper.hasLiked(blogId, userNumber) > 0) {
            // 取消点赞
            return (like_collectMapper.neverLike(blogId, userNumber) > 0)
                    && (blogMapper.downBlogLike(blogId) > 0);
        } else {
            // 点赞
            return (like_collectMapper.like(
                    UUIDUtil.generateRandomUUIDWithoutHyphens(),
                    blogId,
                    userNumber,
                    new Timestamp(System.currentTimeMillis())
            ) > 0) && (blogMapper.addBlogLike(blogId) > 0);
        }
    }


}
