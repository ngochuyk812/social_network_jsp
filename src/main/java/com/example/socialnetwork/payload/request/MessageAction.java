package com.example.socialnetwork.payload.request;

import lombok.Data;


@Data
public class MessageAction {
    private MessageRequest news;
    private String action;

 }
