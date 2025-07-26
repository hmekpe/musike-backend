package com.musike;

import com.musike.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication (exclude = {SecurityAutoConfiguration.class})
@EnableConfigurationProperties(JwtProperties.class)
public class MusikeBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(MusikeBackendApplication.class, args);
    }
} 