package com.example.socialnetwork.payload.response;

import com.example.socialnetwork.model.MessageModel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatResponse {
    private MessageModel message;
    private String action;
    public static enum ACTION {
        SEND, ENTERING
    }}
