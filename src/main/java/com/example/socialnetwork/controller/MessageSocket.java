package com.example.socialnetwork.controller;


import com.example.socialnetwork.DTO.ParticipantDTO;
import com.example.socialnetwork.api.SendNotificationsExpo;
import com.example.socialnetwork.model.*;
import com.example.socialnetwork.repository.ExpoTokenRepository;
import com.example.socialnetwork.repository.ParticipantRepository;
import com.example.socialnetwork.service.MessageService;
import com.example.socialnetwork.utils.StorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class MessageSocket {
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

//    @MessageMapping("/chat")
//    public String send(@RequestParam Map<String, String> body) throws Exception {
//        System.out.println(body.get("message"));
//        brokerMessagingTemplate.convertAndSend("/topic/messages", "message");
//        return body.get("message");
//    }


    @MessageMapping("/chat/user/{roomID}")
    @SendTo("/queue/specific-user/{roomID}")
    public void handleIncomingMessage(
            @DestinationVariable("roomID") int idRoom,
            @Payload  @RequestParam Map<String, String> body,
            SimpMessageHeaderAccessor simpMessageHeaderAccessor,
            @RequestHeader String host) throws IOException {
        // Sử dụng username và token đã xác nhận ở đây
        String username =(String) simpMessageHeaderAccessor.getSessionAttributes().get("username");
        String action = body.get("action");
        List<Participant> participants =
                participantRepository.findAllByRoom(Room.builder().id(idRoom).build());
//        messageService.sendMessageWithRoomID(to,body,from, host);
        participants.stream().forEach(tmp -> {
            String usernameSend = tmp.getUser().getUsername();
            if(action != null ){
                if((action.equals("ENTERING") || action.equals("BLUR")) && !usernameSend.equals(username)){
                    simpMessagingTemplate.convertAndSend("/queue/specific-user/"+usernameSend, Chat.builder().action(action).build());
                }

            }
        });
    }




}
