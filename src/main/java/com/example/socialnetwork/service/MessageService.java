package com.example.socialnetwork.service;


import com.example.socialnetwork.DTO.RoomDTO;
import com.example.socialnetwork.model.Chat;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public interface MessageService {
    public void sendMessageWithRoomID(int idRoom, String message, String action, String username, String host,
                                      MultipartFile[] files) throws IOException;
    public List<?> getRoomParticipantByUser(String username);
    public List<?> getMessageWithPage(int idRoom, int page);
    public RoomDTO createRoom(List<String> usernames);

}
