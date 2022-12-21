package com.yc.community;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@ComponentScan(basePackages = "com.yc.community.*")
@MapperScan(value = {"com.yc.community.sys.mapper","com.yc.community.service.modules.articles.mapper"})
@EnableAsync
public class FishCommunityApplication {
    public static void main(String[] args) {
        SpringApplication.run(FishCommunityApplication.class, args);
    }
}
