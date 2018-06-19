package com.s3.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.s3.config.S3Config;
import com.s3.exception.S3Exception;

import javax.inject.Inject;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
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
        try {
            String key = UUID.randomUUID().toString();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("plain/text");
            amazonS3Client.putObject(config.getBucketName(), key, fileInputStream, metadata);
            return key;
        } catch(AmazonServiceException ex) {
            throw new S3Exception(ex, "Amazon S3 couldn't process it");
        } catch(SdkClientException ex) {
            throw new S3Exception(ex, "Amazon S3 couldn't be contacted for a response, or the client couldn't parse the response from Amazon S3.");
        }
    }

    public String getFileUrl(String token) {
        try {
            Date expiration = setExpirationTimeToOneMinute();
            GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedUrlRequest(token, expiration);
            URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
            return url.toString();
        } catch(SdkClientException ex) {
            throw new S3Exception(ex, "Amazon S3 couldn't be contacted for a response, or the client couldn't parse the response from Amazon S3.");
        }
    }

    private GeneratePresignedUrlRequest getGeneratePresignedUrlRequest(String token, Date expiration) {
        return new GeneratePresignedUrlRequest(config.getBucketName(), token)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
    }

    private Date setExpirationTimeToOneMinute() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60;
        expiration.setTime(expTimeMillis);
        return expiration;
    }
}
