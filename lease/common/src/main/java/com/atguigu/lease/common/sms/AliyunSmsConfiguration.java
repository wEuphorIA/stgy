package com.atguigu.lease.common.sms;

import com.aliyun.credentials.models.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 @author Euphoria
 @version 1.0
 @description: TODO
 @date 2025/9/17 下午9:05 */
@Configuration
public class AliyunSmsConfiguration {
    @Bean
    public com.aliyun.dysmsapi20170525.Client smsClient() {
        Config config1 = new Config();
        config1.setType("access_key");
        config1.setAccessKeyId(System.getenv("OSS_ACCESS_KEY_ID"));
        config1.setAccessKeySecret(System.getenv("OSS_ACCESS_KEY_SECRET"));
        com.aliyun.credentials.Client credential = new com.aliyun.credentials.Client(config1);


        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setCredential(credential);
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = "dysmsapi.aliyuncs.com";
        try {
            return new com.aliyun.dysmsapi20170525.Client(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
