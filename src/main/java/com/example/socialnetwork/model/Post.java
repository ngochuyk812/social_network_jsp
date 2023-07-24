package com.example.socialnetwork.model;


import com.example.socialnetwork.DTO.PostDTO;
import com.example.socialnetwork.model.key.FriendKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String caption;
    private int audience;
    private int layout;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private User user;
    @OneToMany(fetch = FetchType.LAZY, mappedBy ="post")
    private List<MediaPost> mediaPosts;
    @Basic(optional = false)
    @Column(name="create_at", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Date createAt;
    private int status;


    public PostDTO toDTO(){
        return PostDTO.builder()
                .userDTO(user.toDTO())
                .medias(mediaPosts.stream().map(tmp->tmp.toDTO()).toList())
                .caption(caption)
                .audience(audience)
                .layout(layout)
                .createAt(createAt)
                .build();
    }
}
