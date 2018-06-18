package com.s3.service

import com.amazonaws.services.s3.AmazonS3
import com.s3.config.S3Config
import spock.lang.Specification

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath

class S3ServiceSpec extends Specification {

    def "should upload the file to S3 and return the key"() {
        given:
        def amazonS3Client = Mock(AmazonS3)
        def config = new S3Config("test-bucket", "accessKey", "Secret")
        def s3Service = new S3Service(amazonS3Client, config)
        def fileInputStream = new FileInputStream(new File(resourceFilePath("testFile.txt")))

        when:
        def key = s3Service.upload(fileInputStream)

        then:
        1 * amazonS3Client.putObject(config.getBucketName(),_ as String, fileInputStream, _ )
        key != null
    }
}
