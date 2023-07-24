package com.example.socialnetwork.utils;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.*;

@Service
@Slf4j
@Getter

public class AmazonClient {

    private AmazonS3 s3client;

    @Value("${aws.endpointUrl}")
    private String endpointUrl;
    @Value("${aws.bucketName}")
    private String bucketName;
    @Value("${aws.accessKey}")
    private String accessKey;
    @Value("${aws.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_SOUTHEAST_2)
                .enablePathStyleAccess()
                .disableChunkedEncoding()
                .withPathStyleAccessEnabled(true)
                .build();
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }
    private File convertMultiPartToFile(MultipartFile file,String firstName) throws IOException {
        String type = ".";
        if(file.getContentType().contains("video")){
            type += "mp4";
        }
        if(file.getContentType().contains("image")){
            type += "jpg";
        }
        File convFile = new File(firstName + "_" + new Date().getTime() + new Random(1000).nextInt() + type);
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
    private String generateUrlPublic(String fileName, HttpMethod httpMethod) {
        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, fileName).withMethod(httpMethod);
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 5 * 60 * 1000;
        expiration.setTime(expTimeMillis);
        urlRequest.setExpiration(expiration);
        AccessControlList acl = new AccessControlList();

        // Áp dụng quyền truy cập tùy chỉnh cho Object
        urlRequest.addRequestParameter("x-amz-acl", "public-read");

        return s3client.generatePresignedUrl(urlRequest).toString();
    }
    private String generateUrlPrivate(String fileName, HttpMethod httpMethod) {
        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, fileName).withMethod(httpMethod);
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 5 * 60 * 1000;
        expiration.setTime(expTimeMillis);
        urlRequest.setExpiration(expiration);
        AccessControlList acl = new AccessControlList();
        // Áp dụng quyền truy cập tùy chỉnh cho Object
        urlRequest.addRequestParameter("x-amz-room-id", "public-read");

        return s3client.generatePresignedUrl(urlRequest).toString();
    }


    public String uploadFile(MultipartFile multipartFile, String firstName) {

        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile, "media_message");
            String fileName = file.getName();
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    public static void main(String[] args) {
        String accessKey = "AKIASRMCHQXBHJCNLGFM";
        String secretKey = "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";
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

         AmazonS3 s3client;
        s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_SOUTHEAST_2)
                .enablePathStyleAccess()
                .withPathStyleAccessEnabled(true)
                .disableChunkedEncoding()
                .build();
        GetObjectRequest getObjectRequest = new GetObjectRequest("social-network-nnh", "54272ebf-f3d7-43dc-a114-cd88026c27e1.jpg");
        S3Object object = s3client.getObject(getObjectRequest);
        InputStream fileContent = object.getObjectContent();

// Đọc nội dung của tệp tin
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileContent));
        String line;
        StringBuilder content = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc tệp tin từ Amazon S3: " + e.getMessage());
        } finally {
            // Đóng luồng và tài nguyên khi đã xong
            try {
                fileContent.close();
                reader.close();
            } catch (IOException e) {
                System.err.println("Lỗi khi đóng luồng đọc tệp tin: " + e.getMessage());
            }
        }
        String outputFile = "C:\\Config\\file.jpg";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(content.toString());
            writer.flush();
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi tệp tin mới: " + e.getMessage());
        }
    }

}