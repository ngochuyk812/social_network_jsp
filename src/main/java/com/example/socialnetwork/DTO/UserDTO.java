package com.example.socialnetwork.DTO;


import com.example.socialnetwork.model.Message;
import com.example.socialnetwork.model.Participant;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String fullName;
    private Date birthDay;
    private String email;
    private String address;
    private String story;
    private int status;

}
