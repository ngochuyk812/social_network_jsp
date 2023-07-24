package com.example.socialnetwork.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class PostDTO {
    private List<MediaPostDTO> medias;
    private String caption;
    private int audience;
    private int layout;
    private UserDTO userDTO;
    private Date createAt;
}
