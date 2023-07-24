package com.example.socialnetwork.service.imp;

import com.example.socialnetwork.DTO.MessageDTO;
import com.example.socialnetwork.DTO.RoomDTO;
import com.example.socialnetwork.DTO.UserDTO;
import com.example.socialnetwork.api.SendNotificationsExpo;
import com.example.socialnetwork.model.*;
import com.example.socialnetwork.repository.*;
import com.example.socialnetwork.service.FriendService;
import com.example.socialnetwork.service.MessageService;
import com.example.socialnetwork.utils.StorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@Slf4j
public class FriendeServiceImp implements FriendService {
    @Autowired
    private FriendRepository friendRepository;
    @Override
    public List<UserDTO> getFriend(String username) {
        List<UserDTO> rs = new ArrayList<UserDTO>();
        List<Friend> friends =
                friendRepository.findAllByUsernameOneOrUsernameTwo(User.builder().username(username).build(), User.builder().username(username).build());
        friends.stream().forEach(tmp->{
            if(tmp.getUsernameTwo().getUsername().equals(username)){
                rs.add(tmp.getUsernameOne().toDTO());
            }else{
                rs.add(tmp.getUsernameTwo().toDTO());
            }
        });
        return rs;
    }
}
