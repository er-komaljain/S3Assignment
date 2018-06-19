package com.s3.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class S3Config {
    @NotEmpty
    private String bucketName;

    @NotEmpty
    private String accessKeyId;

    @NotEmpty
    private String accessSecretKey;

    @JsonCreator
    public S3Config(@JsonProperty("bucketName") String bucketName,
                    @JsonProperty("accessKeyId") String accessKeyId,
                    @JsonProperty("accessSecretKey") String accessSecretKey) {
        this.bucketName = bucketName;
        this.accessKeyId = accessKeyId;
        this.accessSecretKey = accessSecretKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getAccessSecretKey() {
        return accessSecretKey;
    }
}
