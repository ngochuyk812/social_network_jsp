package com.example.socialnetwork.model;


import com.example.socialnetwork.DTO.UserDTO;
import com.example.socialnetwork.model.key.FriendKey;
import com.example.socialnetwork.model.key.TokenExpoKey;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(FriendKey.class)
public class Friend {
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username_one", referencedColumnName = "username")
    private User usernameOne;
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username_two", referencedColumnName = "username")
    private User usernameTwo;
    private int status;
}
