package com.s3.service

import com.amazonaws.AmazonServiceException
import com.amazonaws.SdkClientException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.s3.config.S3Config
import spock.lang.Specification

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath
import static java.time.Instant.now
import static java.time.temporal.ChronoUnit.MINUTES

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

    def "should throw Exception if s3 client return AmazonServiceException"() {
        given:
        def amazonS3Client = Mock(AmazonS3)
        def config = new S3Config("test-bucket", "accessKey", "Secret")
        def s3Service = new S3Service(amazonS3Client, config)
        def fileInputStream = new FileInputStream(new File(resourceFilePath("testFile.txt")))

        1 * amazonS3Client.putObject(config.getBucketName(),_ as String, fileInputStream, _ ) >> {throw new AmazonServiceException()}

        when:
        s3Service.upload(fileInputStream)

        then:
        thrown(Exception)
    }

    def "should throw Exception if s3 client return SdkClientException"() {
        given:
        def amazonS3Client = Mock(AmazonS3)
        def config = new S3Config("test-bucket", "accessKey", "Secret")
        def s3Service = new S3Service(amazonS3Client, config)
        def fileInputStream = new FileInputStream(new File(resourceFilePath("testFile.txt")))

        1 * amazonS3Client.putObject(config.getBucketName(),_ as String, fileInputStream, _ ) >> {throw new SdkClientException()}

        when:
        s3Service.upload(fileInputStream)

        then:
        thrown(Exception)
    }

    def "should get pre signed url from S3 valid for 1 minute for given token"() {
        given:
        def token = "token"
        def amazonS3Client = Mock(AmazonS3)
        def config = new S3Config("test-bucket", "accessKey", "Secret")
        def s3Service = new S3Service(amazonS3Client, config)
        def s3Url = new URL("http://baseurl/s3-url")


        when:
        def url = s3Service.getFileUrl(token)

        then:
        1 * amazonS3Client.generatePresignedUrl(_) >> { arguments ->
            GeneratePresignedUrlRequest request = arguments[0]
            request.bucketName == config.getBucketName()
            request.expiration.toInstant().isBefore(now().plus(1, MINUTES))
            return s3Url
        }

        url == "http://baseurl/s3-url"
    }

    def "should throw Exception if s3 client return SdkClientException when generating URL"() {
        given:
        def amazonS3Client = Mock(AmazonS3)
        def config = new S3Config("test-bucket", "accessKey", "Secret")
        def s3Service = new S3Service(amazonS3Client, config)
        def token = "someToken"

        1 * amazonS3Client.generatePresignedUrl(_) >> {throw new SdkClientException("Some message")}

        when:
        s3Service.getFileUrl(token)

        then:
        thrown(Exception)
    }

}
