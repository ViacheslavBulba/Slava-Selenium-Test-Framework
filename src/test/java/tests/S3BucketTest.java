package tests;

import org.testng.annotations.Test;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static utils.S3BucketReader.getNameOfTheLastFileInTheBucket;
import static utils.S3BucketReader.readAllBytes;

public class S3BucketTest {

    @Test
    public void awsS3BucketTest() throws IOException {
        System.setProperty("aws.accessKeyId", ""); // TODO ADD YOUR KEY HERE
        System.setProperty("aws.secretAccessKey", ""); // TODO ADD YOUR KEY HERE
        String bucketName = "slava-s3-bucket-test";
        String folderInsideBucket = "test-folder";

        Region region = Region.US_WEST_1;
        S3Client s3 = S3Client.builder()
                .region(region)
                .build();

        String jsonFilePath = getNameOfTheLastFileInTheBucket(s3, bucketName, folderInsideBucket);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(jsonFilePath)
                .build();
        ResponseInputStream<GetObjectResponse> responseInputStream = s3.getObject(getObjectRequest);
        System.out.println("File Content:");
        System.out.println();
        System.out.println(new String(readAllBytes(responseInputStream), StandardCharsets.UTF_8));
        s3.close();
    }

}
