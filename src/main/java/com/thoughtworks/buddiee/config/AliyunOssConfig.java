package com.thoughtworks.buddiee.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyunOssConfig {

    private String bucketName;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
}
