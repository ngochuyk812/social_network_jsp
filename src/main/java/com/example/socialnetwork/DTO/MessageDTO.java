package com.example.socialnetwork.DTO;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Builder
@Data
public class MessageDTO {
    private int id;
    private int room;
    private UserDTO user;
    private List<MediaMessageDTO> mediaMessages;
    private Date createAt;
    private boolean status;



}
