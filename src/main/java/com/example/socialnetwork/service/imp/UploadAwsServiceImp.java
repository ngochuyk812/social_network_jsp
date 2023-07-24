package com.example.socialnetwork.service.imp;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.socialnetwork.DTO.UserDTO;
import com.example.socialnetwork.model.Friend;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.repository.FriendRepository;
import com.example.socialnetwork.service.FriendService;
import com.example.socialnetwork.service.UploadAwsService;
import com.example.socialnetwork.utils.AmazonClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class UploadAwsServiceImp implements UploadAwsService {
    @Autowired
    private AmazonClient amazonClient;
    @Override
    public String ganerateNameRandom(String extension) {
        return UUID.randomUUID().toString() + extension;
    }

    @Override
    public String generateUrlPublic(String fileName, HttpMethod httpMethod) {
        GeneratePresignedUrlRequest urlRequest =
                new GeneratePresignedUrlRequest(amazonClient.getBucketName(), fileName).withMethod(httpMethod);
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 5 * 60 * 1000;
        expiration.setTime(expTimeMillis);
        urlRequest.setExpiration(expiration);
        AccessControlList acl = new AccessControlList();
        // Áp dụng quyền truy cập tùy chỉnh cho Object
        urlRequest.addRequestParameter("x-amz-acl", "public-read");

        return amazonClient.getS3client().generatePresignedUrl(urlRequest).toString();
    }

    @Override
    public String generateUrlPrivate(String fileName,String keySecurity, HttpMethod httpMethod) {
        GeneratePresignedUrlRequest urlRequest =
                new GeneratePresignedUrlRequest(amazonClient.getBucketName(), fileName).withMethod(httpMethod);
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 5 * 60 * 1000;
        expiration.setTime(expTimeMillis);
        urlRequest.setExpiration(expiration);
        AccessControlList acl = new AccessControlList();
        urlRequest.addRequestParameter("x-amz-security", keySecurity);

        return amazonClient.getS3client().generatePresignedUrl(urlRequest).toString();
    }
}
