package com.s3.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.s3.config.S3Config;

import javax.inject.Inject;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class S3Service {

    private final AmazonS3 amazonS3Client;
    private final S3Config config;

    @Inject
    public S3Service(AmazonS3 amazonS3Client, S3Config s3Config) {
        this.amazonS3Client = amazonS3Client;
        this.config = s3Config;
    }

    public String upload(InputStream fileInputStream) {
        String key = UUID.randomUUID().toString();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("plain/text");
        amazonS3Client.putObject(config.getBucketName(), key, fileInputStream, metadata);
        return key;
    }

    public String downloadFile(String token) {
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60;
        expiration.setTime(expTimeMillis);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(config.getBucketName(), token)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }
}
