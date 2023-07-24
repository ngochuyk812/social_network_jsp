package com.example.socialnetwork.model;
import com.example.socialnetwork.DTO.ParticipantDTO;
import com.example.socialnetwork.DTO.RoomDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int type;
    @OneToMany(fetch = FetchType.LAZY, mappedBy ="room")
    @ToString.Exclude
    private List<Participant> participants;
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy ="room")
    private List<Message> messages;

    public RoomDTO toDTO(){

        return RoomDTO.builder()
                .id(id)
                .messages(messages.stream().map(tmp->tmp.toDTO()).toList())
                .name(name)
                .type(type)
                .build();
    }




}
