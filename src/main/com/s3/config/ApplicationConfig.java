package com.s3.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;

public class ApplicationConfig extends Configuration {

    @Valid
    private S3Config s3Config;

    @JsonCreator
    public ApplicationConfig(@JsonProperty("s3Config") S3Config s3Config) {
        this.s3Config = s3Config;
    }

    public S3Config getS3Config() {
        return s3Config;
    }
}
