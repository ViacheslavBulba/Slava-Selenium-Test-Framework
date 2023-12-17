package utils;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class S3BucketReader {

    public static String getNameOfTheLastFileInTheBucket(S3Client s3, String bucketName, String folderPath) {
        String jsonFilePath = "";
        try {
            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(bucketName)
                    .build();
            ListObjectsResponse res = s3.listObjects(listObjects);
            List<S3Object> s3objects = res.contents();
            List<S3Object> s3objectsInTargetFolder = new ArrayList<>();
            System.out.println();
            System.out.println("All objects found in the bucket:");
            for (S3Object s3Object : s3objects) {
                System.out.println("    " + s3Object.key());
                if (s3Object.key().contains(folderPath)) {
                    s3objectsInTargetFolder.add(s3Object);
                }
            }
            System.out.println();
            String lastFile = s3objectsInTargetFolder.get(s3objectsInTargetFolder.size() - 1).key();
            System.out.println("Last File: " + lastFile);
            jsonFilePath = lastFile;
            System.out.println();
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return jsonFilePath;
    }

    public static byte[] readAllBytes(InputStream inputStream) throws IOException {
        final int bufLen = 1024;
        byte[] buf = new byte[bufLen];
        int readLen;
        IOException exception = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            while ((readLen = inputStream.read(buf, 0, bufLen)) != -1)
                outputStream.write(buf, 0, readLen);
            return outputStream.toByteArray();
        } catch (IOException e) {
            exception = e;
            throw e;
        } finally {
            if (exception == null) inputStream.close();
            else try {
                inputStream.close();
            } catch (IOException e) {
                exception.addSuppressed(e);
            }
        }
    }

}
