package com.example.socialnetwork.model;


import com.example.socialnetwork.DTO.ParticipantDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "participants")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public ParticipantDTO toDTO(){
        return ParticipantDTO.builder()
                .id(id)
                .roomId(room.getId())
                .user(user.toDTO())
                .build();
    }

}
