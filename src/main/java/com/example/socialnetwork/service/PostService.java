package com.example.socialnetwork.service;

import com.example.socialnetwork.DTO.PostDTO;
import com.example.socialnetwork.DTO.UserDTO;
import com.example.socialnetwork.payload.request.PostData;

import java.util.List;

public interface PostService {


    public List<String> initPresignedUrls(List<String> expe);
    public PostDTO uploadPost(PostData postData);
    public List<PostDTO> getAllPost();
}
