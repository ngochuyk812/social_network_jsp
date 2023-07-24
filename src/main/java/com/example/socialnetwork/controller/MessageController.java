package com.example.socialnetwork.controller;


import com.example.socialnetwork.DTO.MessageDTO;
import com.example.socialnetwork.DTO.ParticipantDTO;
import com.example.socialnetwork.DTO.RoomDTO;
import com.example.socialnetwork.model.Participant;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.repository.ParticipantRepository;
import com.example.socialnetwork.repository.RoomRepository;
import com.example.socialnetwork.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/chat")
public class MessageController {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private MessageService messageService;
        @GetMapping("/get_room_by_user")
    public ResponseEntity<?> getRoomByUser(@RequestAttribute(name = "username") String username, @RequestHeader String host){
        System.out.println("GET ROOM BY " + username);
        List<RoomDTO> roomDTOS = (List<RoomDTO>) messageService.getRoomParticipantByUser(username);
        roomDTOS.forEach(tmp->{
            tmp.getMessages().forEach(message->{
                message.getMediaMessages().forEach(media->{
                    if(!media.getType().equals("TEXT") ){
                        media.setSrc("http://"+(host +"\\" + media.getSrc()).replace("\\", "/"));
                    }
                });
            });
            });
        return ResponseEntity.ok().body(roomDTOS);
    }
    @GetMapping("/get_detail_message_page")
    public ResponseEntity<?> getDetailMessage(@RequestParam(name = "page") int page,
                                              @RequestParam(name = "room") int idRoom,
                                              @RequestHeader String host
                                            ){
        System.out.println("GET DETAIL MESSAGE " + idRoom);
        List<MessageDTO> list = (List<MessageDTO>) messageService.getMessageWithPage(idRoom, page);
        System.out.println();
        list.forEach(tmp->{
            tmp.getMediaMessages().forEach(tmp2->{
                if(!tmp2.getType().equals("TEXT")){
                    tmp2.setSrc("http://"+(host +"\\" + tmp2.getSrc()).replace("\\", "/"));

                }
            });
        });
//        System.out.println(request.getRequestURL() + " ---- " + request.getServletPath() + " ----- " + request.getServerName());
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/send_message")
    public ResponseEntity<?> getUserByRoom(@RequestParam(value = "files", required = false)  MultipartFile[] files,
                                           @RequestParam("message") String message,
                                           @RequestParam("action") String action,
                                           @RequestParam("idRoom") int idRoom,
                                           @RequestHeader String host,
                                           @RequestAttribute("username" )String username
    ) throws IOException {
        if(files != null){
            log.info(files.toString() + "------"+ message + "---------" + action + "---------------" + idRoom) ;

        }
        log.info( "------"+ message + "---------" + action + "---------------" + idRoom) ;

        if(files == null && message.equals(""))
            return ResponseEntity.ok().body("");
        messageService.sendMessageWithRoomID(idRoom,message,action,username,host,files);
        return ResponseEntity.ok().body("");
    }

    @PostMapping("/create_room")
    public ResponseEntity<?> createRoom(@RequestParam Map<String, ?> body ) throws ParseException {
        String getUsername = (String) body.get("usernames");
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(getUsername);
        List<String> usernames = new ArrayList<>();
        JSONArray array = (JSONArray)obj;

        for (Object tmp: array) {
            System.out.println(tmp);
            usernames.add((String) tmp);

        }
        return ResponseEntity.ok().body(messageService.createRoom(usernames));

    }



}
