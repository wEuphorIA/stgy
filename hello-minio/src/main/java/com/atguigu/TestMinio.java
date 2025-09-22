package com.atguigu;

import io.minio.*;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 @author Euphoria
 @version 1.0
 @description: TODO
 @date 2025/8/20 下午1:10 */
public class TestMinio {
    public static void main(String[] args) {
        String endPoint = "http://192.168.200.128:9000";
        String accessKey = "minioadmin";
        String secretKey = "minioadmin";
        String bucketName = "hello-minio";
        String ip = "http://192.168.200.128:9000/hello-minio/公寓-外观.jpg";
        MinioClient minioClient = MinioClient.builder().endpoint(endPoint).credentials(accessKey, secretKey).build();
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                String policy = """
                        {
                          "Statement" : [ {
                            "Action" : "s3:GetObject",
                            "Effect" : "Allow",
                            "Principal" : "*",
                            "Resource" : "arn:aws:s3:::hello-minio/*"
                          } ],
                          "Version" : "2012-10-17"
                        }
                        """;
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket("hello-minio").config(policy).build());
                //上传图片
                minioClient.uploadObject(
                        UploadObjectArgs.builder()
                                .bucket("hello-minio")
                                .object("公寓-外观.jpg")
                                .filename("C:\\Users\\wyk\\Desktop\\课件\\尚庭公寓\\2.资料\\7.images\\公寓-外观.jpg")
                                .build());

                System.out.println("成功");

        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
