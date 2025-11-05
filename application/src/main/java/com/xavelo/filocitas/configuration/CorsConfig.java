package com.xavelo.filocitas.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "https://preview-philosophy-quotes-ui-*",
                        "https://filocitas.vercel.app*",
                        "http://localhost:5173"
                )
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}
