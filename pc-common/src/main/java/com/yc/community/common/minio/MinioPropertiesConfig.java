package com.yc.community.common.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * description: minio配置类
 *
 *
 */
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioPropertiesConfig {

    /**
     * 端点
     */
    private String endpoint;

    private Integer port;

    private Boolean secure;
    /**
     * 用户名
     */
    private String accessKey;
    /**
     * 密码
     */
    private String secretKey;

    /**
     * 桶名称
     */
    private String bucketName;
}
