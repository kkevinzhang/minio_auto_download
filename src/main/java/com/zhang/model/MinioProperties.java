package com.zhang.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    private String minioRootUser;

    private String minioRootPassword;

    private String minioUrl;

    private String minioBucket;

    private String fileStoragePath;
}

