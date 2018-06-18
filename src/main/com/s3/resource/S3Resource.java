package com.s3.resource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/s3")
public class S3Resource {
    @POST
    @Path("upload")
    public Response uploadFile() {
        return null;
    }
}
