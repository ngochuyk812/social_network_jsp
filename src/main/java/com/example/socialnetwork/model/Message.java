package com.example.socialnetwork.model;


import com.example.socialnetwork.DTO.MediaMessageDTO;
import com.example.socialnetwork.DTO.MessageDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private User username;
    @OneToMany(fetch = FetchType.LAZY, mappedBy ="message")
    private List<MediaMessage> mediaMessages;
    @Column(columnDefinition = "boolean default false")
    private boolean status;
    @Basic(optional = false)
    @Column(name="create_at", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Date createAt;


    public MessageDTO toDTO(){
        return MessageDTO.builder()
                .id(id)
                .room(room.getId())
                .user(username.toDTO())
                .createAt(createAt)
                .mediaMessages(toMediaMessageDTO())
                .status(status)
                .build();
    }
    private List<MediaMessageDTO> toMediaMessageDTO(){
        List<MediaMessageDTO> rs = new ArrayList<>();
        for (MediaMessage tmp: mediaMessages) {
            rs.add(tmp.toDTO());
        }
        return rs;
    }
}
