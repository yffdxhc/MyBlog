package org.nuist.myblog.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringBootWebConfigurer implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(SpringBootWebConfigurer.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.info("Adding resource handler for /avatar/** to file:D:\\MyBlogPath\\avatar\\");
        logger.info("Adding resource handler for /md/** to file:D:\\MyBlogPath\\md\\");
        registry.addResourceHandler("/avatar/**").addResourceLocations("file:D:\\MyBlogPath\\avatar\\");
        registry.addResourceHandler("/cover/**").addResourceLocations("file:D:\\MyBlogPath\\cover\\");
        registry.addResourceHandler("/blog/**").addResourceLocations("file:D:\\MyBlogPath\\blog\\");
        registry.addResourceHandler("/md/**").addResourceLocations("file:D:\\MyBlogPath\\md\\");
    }
}
