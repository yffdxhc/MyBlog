package org.nuist.myblog.service;

import org.nuist.myblog.entity.Blog;
import org.nuist.myblog.entity.User;
import org.nuist.myblog.mapper.BlogMapper;
import org.nuist.myblog.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {
    private static final Logger log = LoggerFactory.getLogger(BlogService.class);

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private UserMapper userMapper;

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
}
