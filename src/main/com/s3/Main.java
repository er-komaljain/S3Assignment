package com.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.s3.config.ApplicationConfig;
import com.s3.resource.S3Resource;
import com.s3.service.S3Service;
import io.dropwizard.Application;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class Main extends Application<ApplicationConfig> {

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

    @Override
    public void initialize(Bootstrap<ApplicationConfig> bootstrap) {
        bootstrap.addBundle(new MultiPartBundle());
    }

    @Override
    public void run(ApplicationConfig configuration, Environment environment) throws Exception {
        JerseyEnvironment jersey = environment.jersey();
        AWSCredentials credentials = new BasicAWSCredentials(
                configuration.getS3Config().getAccessKeyId(),
                configuration.getS3Config().getAccessSecretKey()
        );

        AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_SOUTH_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        S3Service s3Service = new S3Service(amazonS3Client, configuration.getS3Config());
        jersey.register(new S3Resource(s3Service));
    }
}
