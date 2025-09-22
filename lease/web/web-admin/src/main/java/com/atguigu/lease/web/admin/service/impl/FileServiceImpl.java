package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.common.minio.MinioProperties;
import com.atguigu.lease.web.admin.service.FileService;
import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private MinioProperties properties;

    @Autowired
    @Qualifier("minioClient")
    private MinioClient minioClient;

    @Override
    public String upload(MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(properties.getBucketName()).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(properties.getBucketName()).build());
            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(properties.getBucketName()).config(createBucketPolicyConfig(properties.getBucketName())).build());
        }
        String filename = DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now())
                + "/"
                + UUID.randomUUID()
                + "-"
                + file.getOriginalFilename();
        minioClient.putObject(PutObjectArgs.builder().bucket(properties.getBucketName()).stream(file.getInputStream(), file.getSize(), -1).object(filename).contentType(file.getContentType()).build());
        return properties.getEndpoint() + "/" + properties.getBucketName() + "/" + filename;

    }

    private String createBucketPolicyConfig(String bucketName) {

        return """
                {
                  "Statement" : [ {
                    "Action" : "s3:GetObject",
                    "Effect" : "Allow",
                    "Principal" : "*",
                    "Resource" : "arn:aws:s3:::%s/*"
                  } ],
                  "Version" : "2012-10-17"
                }
                """.formatted(bucketName);
    }
}