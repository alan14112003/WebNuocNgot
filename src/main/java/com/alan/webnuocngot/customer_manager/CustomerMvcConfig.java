package com.alan.webnuocngot.customer_manager;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class CustomerMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path customerUploadDir = Paths.get("./customer-avatars");
        String customerUploadPath = customerUploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/customer-avatars/**").addResourceLocations("file:/"+ customerUploadPath +"/");
    }
}
