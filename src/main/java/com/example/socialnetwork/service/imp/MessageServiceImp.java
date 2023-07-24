package com.example.socialnetwork.service.imp;

import com.example.socialnetwork.DTO.MessageDTO;
import com.example.socialnetwork.DTO.ParticipantDTO;
import com.example.socialnetwork.DTO.RoomDTO;
import com.example.socialnetwork.api.SendNotificationsExpo;
import com.example.socialnetwork.model.*;
import com.example.socialnetwork.repository.*;
import com.example.socialnetwork.service.MessageService;
import com.example.socialnetwork.utils.StorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class MessageServiceImp implements MessageService {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private MediaMessageRepository mediaMessageRepository;
    @Autowired
    private StorageProperties storageProperties;
    @Autowired
    private ExpoTokenRepository expoTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public void sendMessageWithRoomID(int idRoom, String message, String action, String username, String host,
                                      MultipartFile[] files) throws IOException {


        MessageDTO messageDTO;
        List<Participant> participants =
                participantRepository.findAllByRoom(Room.builder().id(idRoom).build());
        if(action != null && action.equals("SEND")){
            List<MediaMessage> linkMedia = new ArrayList<>();
            Message messageEntity = Message.builder()
                    .room(Room.builder().id(idRoom).build())
                    .username(User.builder().username(username).build()).build();
            messageEntity = messageRepository.save(messageEntity);


            if (files != null) {
                try {
                    linkMedia = saveMedia(files, messageEntity);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(message != null){
                MediaMessage mediaMessage = MediaMessage.builder()
                        .message(messageEntity)
                        .type(0)
                        .src(message)
                        .build();
                linkMedia.add(mediaMessage);
            }
            linkMedia = mediaMessageRepository.saveAll(linkMedia);
            linkMedia.forEach(tmp->{
                if(tmp.getType() == 1){
                    tmp.setSrc("http://"+(host +"\\" + tmp.getSrc()).replace("\\", "/"));
                }
            });
            messageEntity.setMediaMessages(linkMedia);
             messageDTO = messageEntity.toDTO();
        } else {
            messageDTO = null;
        }
        participants.stream().forEach(tmp -> {
            String usernameSend = tmp.getUser().getUsername();
            if(action != null ){
                if((action.equals("ENTERING") || action.equals("BLUR")) && !usernameSend.equals(username)){
                    simpMessagingTemplate.convertAndSend("/queue/specific-user/"+usernameSend, Chat.builder().action(action).build());
                }
                if(action.equals("SEND")){
                    //Send Messagee
                    simpMessagingTemplate.convertAndSend("/queue/specific-user/"+usernameSend,
                            Chat.builder().action(action).message(messageDTO).build());
                    if(!usernameSend.equals(username)){
                        List<TokenExpo> tokenExpos = expoTokenRepository.findTokenExposByUsername(usernameSend);
                        for(TokenExpo tmp2: tokenExpos){
                            System.out.println(tmp2.toString() + "TOKEN SEND NOTIFICATION");
                            SendNotificationsExpo.send(tmp2.getTokenExpo(), "Message from " + username, message);
                        }
                    }


                    //End Send Message
                }

            }
        });
    }

//    @Override
//    public void sendMessageWithRoomID(int idRoom, Map<String, ?> body, String username, String host) throws IOException {
//        String message = (String) body.get("message");
//        String action =(String)  body.get("action");
//        ArrayList<?> medias = (ArrayList<?>) body.get("media");
//
//        MessageDTO messageDTO;
//        List<Participant> participants =
//                participantRepository.findAllByRoom(Room.builder().id(idRoom).build());
//        if(action != null && action.equals("SEND")){
//            List<MediaMessage> linkMedia = new ArrayList<>();
//            Message messageEntity = Message.builder()
//                    .room(Room.builder().id(idRoom).build())
//                    .username(User.builder().username(username).build()).build();
//            messageEntity = messageRepository.save(messageEntity);
//
//
//            try {
//                linkMedia = saveMedia(medias, messageEntity);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            if(message != null){
//                MediaMessage mediaMessage = MediaMessage.builder()
//                        .message(messageEntity)
//                        .type(0)
//                        .src(message)
//                        .build();
//                linkMedia.add(mediaMessage);
//            }
//            linkMedia = mediaMessageRepository.saveAll(linkMedia);
//            linkMedia.forEach(tmp->{
//                if(tmp.getType() == 1){
//                    tmp.setSrc("http://"+(host +"\\" + tmp.getSrc()).replace("\\", "/"));
//
//                }
//            });
//            messageEntity.setMediaMessages(linkMedia);
//            messageDTO = messageEntity.toDTO();
//        } else {
//            messageDTO = null;
//        }
//        participants.stream().forEach(tmp -> {
//            String usernameSend = tmp.getUser().getUsername();
//            if(action != null ){
//                if((action.equals("ENTERING") || action.equals("BLUR")) && !usernameSend.equals(username)){
//                    simpMessagingTemplate.convertAndSend("/queue/specific-user/"+usernameSend, Chat.builder().action(action).build());
//                }
//                if(action.equals("SEND")){
//                    //Send Messagee
//                    simpMessagingTemplate.convertAndSend("/queue/specific-user/"+usernameSend,
//                            Chat.builder().action(action).message(messageDTO).build());
//                    if(!usernameSend.equals(username)){
//                        List<TokenExpo> tokenExpos = expoTokenRepository.findTokenExposByUsername(usernameSend);
//                        for(TokenExpo tmp2: tokenExpos){
//                            System.out.println(tmp2.toString() + "TOKEN SEND NOTIFICATION");
//                            SendNotificationsExpo.send(tmp2.getTokenExpo(), "Message from " + username, message);
//                        }
//                    }
//
//
//                    //End Send Message
//                }
//
//            }
//        });
//    }



    public List<MediaMessage> saveMedia(MultipartFile[] files, Message message) throws IOException {
        List<MediaMessage> rs = new ArrayList<>();
        Arrays.asList(files).stream().forEach(file -> {
            try {
                String path = storageProperties.save(file, "auth/message");
                int type = -1;
                if(file.getContentType().contains("image"))
                    type = 1;
                if(file.getContentType().contains("video"))
                    type = 2;
                log.info(path);
                rs.add(MediaMessage.builder()
                        .src(path)
                        .type(type)
                        .message(message).build());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return rs;
    }



    public List<Message> getFirstNumberMessages(Room room, int number, int page) {
        Pageable pageable = PageRequest.of(page,number, Sort.by("id").descending());
        Page<Message> messageSlice = messageRepository.findAllByRoom(room,  pageable);
        return messageSlice.getContent();
    }



    public List<?> getRoomParticipantByUser(String username){
        List<Room> participants = roomRepository.findRoomsByUsername(username);
        List<RoomDTO> rs = participants.stream().map(tmp->{
            RoomDTO roomDTO= RoomDTO.builder()
                    .id(tmp.getId())
                    .messages(getFirstNumberMessages(tmp, 10, 0).stream().map(tmpMessage->tmpMessage.toDTO()).toList())
                    .name(tmp.getName())
                    .type(tmp.getType())
                    .participants(tmp.getParticipants().stream().map(tmp2->tmp2.toDTO()).toList())
                    .build();
            return roomDTO;
        }).toList();
        return rs;
    }

    @Override
    public List<?> getMessageWithPage(int idRoom, int page) {
        return getFirstNumberMessages(Room.builder().id(idRoom).build(), 50, page).stream().map(tmp->tmp.toDTO()).toList();
    }

    @Override
    public RoomDTO createRoom(List<String> usernames) {
        if(usernames.size() ==0){
            return null;
        }
        Room roomCreate = Room.builder().name("NO_NAME").type(0).build();
        roomCreate = roomRepository.save(roomCreate);
        List<Participant> participants = new ArrayList<>();
        List<User> users = userRepository.findByUsernameIn(usernames);
        for (User user: users
        ) {

            Participant participant =
                    Participant.builder().room(roomCreate).user(user).build();
            participants.add(participant);
        }

        participants = participantRepository.saveAll(participants);

        RoomDTO romDto =
                RoomDTO.builder().id(roomCreate.getId()).name(roomCreate.getName()).type(roomCreate.getType()).messages(new ArrayList<>()).participants(participants.stream().map(tmp->tmp.toDTO()).toList()).build();
        return romDto;
    }
}
