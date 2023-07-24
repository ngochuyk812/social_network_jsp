package com.example.socialnetwork.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data

public class MessageModel {
    private int id ;
    private String to;
    private String from;
    private String mess;
    private String createAt;

}
