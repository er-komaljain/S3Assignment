package com.s3.resource

import com.s3.service.S3Service
import io.dropwizard.testing.junit.ResourceTestRule
import org.glassfish.jersey.media.multipart.FormDataMultiPart
import org.glassfish.jersey.media.multipart.MultiPartFeature
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath

class S3ResourceSpec extends Specification {

    S3Service s3Service = Mock(S3Service)

    @Shared
    @ClassRule
    ResourceTestRule resources = ResourceTestRule.builder()
            .addProvider(MultiPartFeature)
            .addResource(new S3Resource(s3Service))
            .build()

    def "POST /upload should upload a file to S3"() {
        given:
        def file = new File(resourceFilePath("testFile.txt"))
        FormDataMultiPart multipartFileRequest = new FormDataMultiPart()
        multipartFileRequest.field("filename", file.getName());
        multipartFileRequest.bodyPart(new FileDataBodyPart("file", file, new MediaType("txt", "jpeg")))


        when:
        def response = resources.client()
                                .register(MultiPartFeature)
                                .target("/s3/upload")
                                .request()
                                .post(Entity.entity(multipartFileRequest, MediaType.MULTIPART_FORM_DATA))

        then:
        response.status == 200
        1 * s3Service.upload(_ as InputStream)
    }
}
