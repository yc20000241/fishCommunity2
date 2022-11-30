package com.yc.community.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class BeanConfig {

    @Value("${spring.mail.username}")
    private String emailUserName;

    @Value("${spring.mail.password}")
    private String emailPassword;

    @Value("${spring.mail.host}")
    private String emailHost;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String emailAuth;

    @Bean("CustomMailSender")
    public JavaMailSenderImpl customMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setUsername(emailUserName);
        javaMailSender.setPassword(emailPassword);
        javaMailSender.setHost(emailHost);

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", emailAuth);
        javaMailSender.setJavaMailProperties(properties);

        return javaMailSender;
    }
}
