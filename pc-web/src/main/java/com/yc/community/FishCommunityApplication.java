package com.yc.community;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;


@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class,DataSourceAutoConfiguration.class})
public class FishCommunityApplication {
    public static void main(String[] args) {
        SpringApplication.run(FishCommunityApplication.class, args);
    }
}
