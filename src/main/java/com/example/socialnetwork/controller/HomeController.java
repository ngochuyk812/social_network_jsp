package com.example.socialnetwork.controller;


import com.example.socialnetwork.DTO.MessageDTO;
import com.example.socialnetwork.DTO.ParticipantDTO;
import com.example.socialnetwork.model.*;
import com.example.socialnetwork.repository.MessageRepository;
import com.example.socialnetwork.repository.ParticipantRepository;
import com.example.socialnetwork.service.MessageService;
import com.example.socialnetwork.utils.StorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@Slf4j
public class HomeController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private StorageProperties storageProperties;
    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private MessageService messageService;

    @GetMapping("/message/media")
    public ResponseEntity<?> index() throws IOException {
        List<Message> messages = messageRepository.findAll();
        Room room = new Room();
        room.setId(1);
        User user = new User();
        user.setUsername("ngochuymobile");
        MediaMessage mmessage1 = new MediaMessage();
        mmessage1.setSrc("Huy thử");
        mmessage1.setType(0);
        MediaMessage mmessage2 = new MediaMessage();
        mmessage2.setSrc("Huy thử 2");
        mmessage2.setType(0);
        List<MediaMessage> mediaMessages = new ArrayList<>();
        mediaMessages.add(mmessage1);
        mediaMessages.add(mmessage2);

        Message message = new Message();
        message.setUsername(user);
        message.setRoom(room);

    //        Room room = new Room();
//        room.setId(1);
//        User user = new User();
//        user.setUsername("ngochuypc");
//        Message message = Message.builder().room(room).username(user).build();
//        messageRepository.save(message);
        Resource file = storageProperties.load("anh.jpg");
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
    }

}
