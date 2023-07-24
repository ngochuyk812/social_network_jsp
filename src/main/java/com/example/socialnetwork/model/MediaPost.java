package com.example.socialnetwork.model;


import com.example.socialnetwork.DTO.MediaPostDTO;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "post_id")
    @ToString.Exclude
    private Post post;
    private String src;
    private String type;

    public MediaPostDTO toDTO(){
        return MediaPostDTO.builder()
                .src(src)
                .type(type)
                .build();
    }
}
