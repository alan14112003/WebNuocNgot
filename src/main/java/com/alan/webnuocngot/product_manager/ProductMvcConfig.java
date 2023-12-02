package com.alan.webnuocngot.product_manager;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class ProductMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path productUploadDir = Paths.get("./product-images/");
        String productUploadPath = productUploadDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/product-images/**").addResourceLocations("file:/"+ productUploadPath +"/");
    }
}
