package vn.iotstar.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Đường dẫn trên URL: /uploads/**
        // Đường dẫn vật lý: thư mục uploads/ trong project
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}