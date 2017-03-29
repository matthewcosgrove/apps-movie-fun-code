package org.superbiz.moviefun.albums;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.superbiz.moviefun.blobstore.BlobStore;
import org.superbiz.moviefun.blobstore.S3Store;

@SpringBootApplication
public class AlbumServiceApplication {
    public static void main(String... args) {
        SpringApplication.run(AlbumServiceApplication.class, args);
    }

    @Value("${s3.accessKey}") String s3AccessKey;
    @Value("${s3.secretKey}") String s3SecretKey;
    @Value("${s3.bucketName}") String s3BucketName;
    @Value("${s3.endpointUrl}") String s3EndpointUrl;

    @Bean
    public BlobStore blobStore() {
        AWSCredentials credentials = new BasicAWSCredentials(s3AccessKey, s3SecretKey);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);
        s3Client.setEndpoint(s3EndpointUrl);
        final S3ClientOptions clientOptions = S3ClientOptions.builder()
                .setPathStyleAccess(true)
                .disableChunkedEncoding()
                .build();
        s3Client.setS3ClientOptions(clientOptions);

        return new S3Store(s3Client, s3BucketName);
    }
}
