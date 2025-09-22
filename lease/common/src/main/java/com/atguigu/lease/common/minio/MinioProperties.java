package com.atguigu.lease.common.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 @author Euphoria
 @version 1.0
 @description: TODO
 @date 2025/8/22 下午3:55 */
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioProperties {

    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String bucketName;
}