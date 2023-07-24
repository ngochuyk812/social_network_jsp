package com.example.socialnetwork.model;

import com.example.socialnetwork.model.key.TokenExpoKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@IdClass(TokenExpoKey.class)

@Table(name = "token_expo")
public class TokenExpo {
    @Id
    private String username;
    @Id
    @Column(name = "token_expo")
    private String tokenExpo;


}

