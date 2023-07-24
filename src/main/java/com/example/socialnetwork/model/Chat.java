package com.example.socialnetwork.model;

import com.example.socialnetwork.DTO.MessageDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Chat {
    private MessageDTO message;
    private String action;
    public static enum ACTION {
        SEND, ENTERING
    }}
