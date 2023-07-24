package com.example.socialnetwork.service;

import com.example.socialnetwork.DTO.UserDTO;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.payload.response.SignInResponse;

import java.util.List;
import java.util.Map;

public interface FriendService {


    public List<UserDTO> getFriend(String username);
}
