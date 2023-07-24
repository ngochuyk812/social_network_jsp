package com.example.socialnetwork.DTO;
import com.example.socialnetwork.model.Message;
import com.example.socialnetwork.model.Participant;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
public class RoomDTO {
    private int id;
    private String name;
    private int type;
    private List<ParticipantDTO> participants;
    private List<MessageDTO> messages;
}
