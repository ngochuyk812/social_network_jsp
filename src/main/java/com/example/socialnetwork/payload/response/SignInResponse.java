package com.example.socialnetwork.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInResponse {
    private String accessToken;
    private String refreshToken;
    private String username;
    private String fullName;
    private String email;
}
