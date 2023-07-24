package com.example.socialnetwork.payload.request;

import com.example.socialnetwork.DTO.UserDTO;
import lombok.Data;

import java.util.List;

@Data
public class PostData {
    private List<MediaPostData> medias;
    private String caption;
    private int audience;
    private int layout;
    private UserDTO userDTO;
}
