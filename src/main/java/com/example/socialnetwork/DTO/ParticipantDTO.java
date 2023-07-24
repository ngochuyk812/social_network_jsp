package com.example.socialnetwork.DTO;


import com.example.socialnetwork.model.Room;
import com.example.socialnetwork.model.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParticipantDTO {

    private int id;
    private UserDTO user;
    private int roomId;

}
