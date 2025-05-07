package org.nuist.myblog.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.nuist.myblog.entity.Blog;
import org.nuist.myblog.entity.Result;
import org.nuist.myblog.entity.User;
import org.nuist.myblog.service.BlogService;
import org.nuist.myblog.service.UserService;
import org.nuist.myblog.util.JWTUtils;
import org.nuist.myblog.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/blog")
public class BlogController {
    private static final Logger log = LoggerFactory.getLogger(BlogController.class);

    @Value("${app.upload.dir}")
    private String documentRoot;
    @Value("${app.upload.blog.dir}")
    private String blogDir;
    @Value("${app.upload.cover.dir}")
    private String coverDir;
    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;

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

    @PostMapping("/blogRelease")
    public Result<String> blogRelease(HttpServletRequest request,
                                      @RequestParam("blog_title") String title,
                                      @RequestParam("content") String content,
                                      @RequestParam("blog_summary") String summary,
                                      @RequestParam("type_id") Integer type,
                                      @RequestParam("cover_image") MultipartFile cover) {
        Result<String> result = new Result<>();
        String blogId = UUIDUtil.generateRandomUUIDWithoutHyphens();
        try {
            // 从拦截器中传来的 request 属性中获取用户信息
            String token = request.getHeader("token");
            DecodedJWT verify = JWTUtils.verify(token);
            String userId = verify.getClaim("user_id").asString();
            String userName = verify.getClaim("userName").asString();
            String userNumber = verify.getClaim("user_number").asString();

            if (userNumber==null){
                result.setSuccess(false);
                result.setMessage("token获取用户信息异常");
                return result;
            }

            log.info("用户id：[{}]", userId);
            log.info("用户名字：[{}]", userName);
            log.info("用户编号：[{}]", userNumber);

            // 保存 Markdown 内容为 .md 文件
            String mdFileName = "/"+blogId + ".md";
            File mdFile = new File(blogDir + mdFileName);
            mdFile.getParentFile().mkdirs();// 确保目录存在
            Files.write(mdFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
            // String mdFilePath = mdFile.getAbsolutePath();
            String mdFilePath = "/blog"+mdFileName;

            // 保存封面图
            String coverFileName = "/"+blogId + "_"+UUIDUtil.generateRandomUUIDWithoutHyphens() + ".jpg";
            File coverFile = new File(coverDir + coverFileName);
            coverFile.getParentFile().mkdirs();// 确保目录存在
            cover.transferTo(coverFile);
            String coverPath = "/cover"+coverFileName;

            // 后续保存数据库逻辑
            Blog blog = new Blog();
            blog.setBlog_id(blogId);
            blog.setBlog_title(title);
            blog.setBlog_content(mdFilePath);
            blog.setBlog_summary(summary);
            blog.setUser_number(userNumber);
            blog.setType_id(type);
            blog.setCover_image(coverPath);
            blog.setBlog_status(1);
            blog.setCreate_time(new Timestamp(System.currentTimeMillis()));
            blog.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            User user = userService.getUserByUserNumber(userNumber);
            blog.setAuthor(user);

            blogService.insertBlog(blog);


            result.setSuccess(true);
            result.setMessage("发布成功");
            result.setData("内容路径：" + mdFilePath + ", 封面路径：" + coverPath+ "，博客信息：" + blog);
        } catch (Exception e) {
            log.error("发布失败", e);
            result.setSuccess(false);
            result.setMessage("发布失败：" + e.getMessage());
        }

        return result;
    }



}
