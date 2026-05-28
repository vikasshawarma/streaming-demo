package com.streamingdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class LocalMediaConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path mediaRoot = Path.of("../media").toAbsolutePath().normalize();
        registry.addResourceHandler("/media/**")
                .addResourceLocations(mediaRoot.toUri().toString());
    }
}
