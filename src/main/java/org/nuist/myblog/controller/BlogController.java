package org.nuist.myblog.controller;

import org.nuist.myblog.entity.Blog;
import org.nuist.myblog.entity.Result;
import org.nuist.myblog.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {
    private static final Logger log = LoggerFactory.getLogger(BlogController.class);

    @Value("${app.upload.dir}")
    private String documentRoot;
    @Autowired
    private BlogService blogService;

    @GetMapping("/getBlogsRecommended")
    public Result<List<Blog>> getBlogsRecommended() {
        Result<List<Blog>> result = new Result<>();
        try {
            List<Blog> blogs = blogService.getBlogs();
            result.setData(blogs);
            result.setSuccess(true);
            result.setMessage("获取推荐博客成功");
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @GetMapping("/getBlogDocument/{blog_id}")
    public Result<String> getBlogDocument(@PathVariable String blog_id) {
        Result<String> result = new Result<>();
        String filePath = null;
        try {
            Blog blog = blogService.getBlogById(blog_id);
            filePath = documentRoot + blog.getBlog_content();

            // 使用兼容Java 8的读取方式
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            String content = new String(bytes, StandardCharsets.UTF_8); // 根据实际编码调整

            result.setData(content);
            result.setSuccess(true);
            result.setMessage("获取博客内容成功");
        } catch (NoSuchFileException e) {
            log.error("文件不存在: {}", filePath);
            result.setMessage("博客内容不存在");
        } catch (IOException e) {
            log.error("文件读取失败: {}", e.getMessage());
            result.setMessage("博客内容读取失败");
        } catch (Exception e) {
            log.error("系统错误: {}", e.getMessage());
            result.setMessage("服务器内部错误");
        }
        return result;
    }

    @GetMapping("/getBlogsSearched")
    public Result<List<Blog>> getBlogsSearched(@RequestParam("query") String query){
        Result<List<Blog>> result = new Result<>();
        try {
            List<Blog> blogs = blogService.getBlogsSearched(query);
            result.setData(blogs);
            result.setSuccess(true);
            result.setMessage("获取搜索结果成功");
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
