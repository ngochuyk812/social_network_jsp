package com.example.socialnetwork.service;

import com.amazonaws.HttpMethod;
import com.example.socialnetwork.DTO.UserDTO;

import java.util.List;

public interface UploadAwsService {
    public String ganerateNameRandom(String extension);
    public String generateUrlPublic(String fileName, HttpMethod httpMethod);
    public String generateUrlPrivate(String fileName, String keySecurity, HttpMethod httpMethod);
}
