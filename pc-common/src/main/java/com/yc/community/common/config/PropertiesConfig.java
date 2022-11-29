package com.yc.community.common.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class PropertiesConfig {

    @Value("${spring.mail.username}")
    public String mailSendFrom;
}
