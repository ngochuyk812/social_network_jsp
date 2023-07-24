package com.example.socialnetwork.service;

import com.example.socialnetwork.DTO.UserDTO;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.payload.response.SignInResponse;

import java.util.List;
import java.util.Map;

public interface UserService {
    public User signUp(Map<String, String> body );
    public SignInResponse signIn(Map<String, String> body );

}
