package org.nuist.myblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JWTInterceptors())
                .addPathPatterns("/user/test")  // 其他接口token验证
                .addPathPatterns("/**")
                .excludePathPatterns("/blog/getBlogsRecommended")
                .excludePathPatterns("/blog/getBlogsSearched")
                .excludePathPatterns("/user/getUserSearched")
                .excludePathPatterns("/blog/getBlogDocument/**")
                .excludePathPatterns("/cover/**")
                .excludePathPatterns("/avatar/**")
                .excludePathPatterns("/user/commonEmail")
                .excludePathPatterns("/blog/getBlogsByUserNumber")
                .excludePathPatterns("/blog/getHotBlogs")
                .excludePathPatterns("/user/loginId");// 所有用户都放行
    }
}

