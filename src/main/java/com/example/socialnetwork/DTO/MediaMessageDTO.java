package com.example.socialnetwork.DTO;

import com.example.socialnetwork.model.Message;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MediaMessageDTO {
    public enum TYPE {
        TEXT, IMAGE, VIDEO, AUDIO
    }
    private int id;
    private int message_id;
    private String src;
    private int type;


    public String getType() {
        switch (type){
            case 0:
               return TYPE.TEXT + "";
            case 1:
                return TYPE.IMAGE + "";
            case 2:
                return TYPE.VIDEO + "";
            case 3:
                return TYPE.AUDIO + "";
            default: return null;
        }
    }
}
