# S3Assignment
Simple REST service to upload a file to S3 and get a Pre signed URL valid for one minute to access file with generated token

Assumptions:
1) No logging
2) AWS secret key and access ID are present as plain string in config without any encryption or security
3) No health check added for service
4) Gradle is installed on your machine (Preferable 3.x)

To start the application:
`gradle run`

To run tests:
`gradle test`

To upload a file :
- POST _http://localhost:8080/s3/upload_
    - Pass the file as form-data with key `file`
    - Returns unique token as string

- GET _http://localhost:8080/s3/{uniqueToken}_
    - Returns Pre signed URL as string with a validity of one minute

