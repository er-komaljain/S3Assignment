package com.s3;

import com.s3.config.ApplicationConfig;
import com.s3.resource.S3Resource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class Main extends Application<ApplicationConfig> {

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

    @Override
    public void run(ApplicationConfig configuration, Environment environment) throws Exception {
        environment.jersey().register(new S3Resource());
    }
}
