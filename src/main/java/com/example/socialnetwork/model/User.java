package com.example.socialnetwork.model;


import com.example.socialnetwork.DTO.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String username;
    private String password;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "birth_day")
    private Date birthDay;

    private String email;
    private String address;
    private String avatar;
    private String banner;
    private String story;
    private int status;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Participant> participants;
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Post> posts;
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "username")
    private List<Message> messages;


    public UserDTO toDTO(){
        return UserDTO.builder()
                .username(username)
                .address(address)
                .status(status)
                .story(story)
                .fullName(fullName)
                .email(email)
                .build();
    }
}
