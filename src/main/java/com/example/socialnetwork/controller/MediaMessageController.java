package com.example.socialnetwork.controller;


import com.example.socialnetwork.model.MediaMessage;
import com.example.socialnetwork.model.Message;
import com.example.socialnetwork.model.Room;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.repository.MessageRepository;
import com.example.socialnetwork.utils.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MediaMessageController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private StorageProperties storageProperties;


        @GetMapping("/get_media_message")
    public ResponseEntity<?> getImageMessage(@RequestParam("path") String path){
        Resource file = storageProperties.load(path);
        return ResponseEntity.ok().contentType(MediaType.ALL).body(file);
    }

}
