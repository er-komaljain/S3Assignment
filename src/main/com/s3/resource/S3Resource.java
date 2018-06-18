package com.s3.resource;

import com.s3.service.S3Service;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path("/s3")
public class S3Resource {

    private S3Service s3Service;

    @Inject
    public S3Resource(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@NotNull @FormDataParam("file") InputStream fileInputStream) {
        String token = s3Service.upload(fileInputStream);
        return Response.ok(token).build();
    }

    @GET
    @Path("{token}")
    public Response downloadFile(@NotNull@PathParam("token") String token) {
        String redirectUrl = s3Service.downloadFile(token);
        return Response.ok(redirectUrl)
                .build();
    }
}
