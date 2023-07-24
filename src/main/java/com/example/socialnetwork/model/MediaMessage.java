package com.example.socialnetwork.model;

import com.example.socialnetwork.DTO.MediaMessageDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "media_message")
public class MediaMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "message_id")
    @ToString.Exclude
    private Message message;
    private String src;
    private int type;

    public MediaMessageDTO toDTO(){
        return MediaMessageDTO.builder()
                .id(id)
                .message_id(message.getId())
                .src(src)
                .type(type)
                .build();
    }

}
