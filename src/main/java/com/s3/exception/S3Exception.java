package com.s3.exception;

import javax.ws.rs.WebApplicationException;

public class S3Exception extends WebApplicationException {

    public S3Exception(Exception exception, String message) {
        super(message, exception);
    }
}
