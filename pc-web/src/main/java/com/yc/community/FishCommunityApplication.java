package com.yc.community;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@ComponentScan(basePackages = "com.yc.community.*")
@MapperScan(value = "com.yc.community.sys.mapper")
public class FishCommunityApplication {
    public static void main(String[] args) {
        SpringApplication.run(FishCommunityApplication.class, args);
    }
}
