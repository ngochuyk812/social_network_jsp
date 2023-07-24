package com.example.socialnetwork.service.imp;

import com.example.socialnetwork.DTO.UserDTO;
import com.example.socialnetwork.config.authencation.CustomUserDetails;
import com.example.socialnetwork.config.authencation.JwtTokenProvider;
import com.example.socialnetwork.model.TokenExpo;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.payload.response.SignInResponse;
import com.example.socialnetwork.repository.ExpoTokenRepository;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class UserServiceImp implements UserService {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExpoTokenRepository expoTokenRepository;

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    public User signUp(Map<String, String> body ){
        User user = User.builder()
                .username(body.get("username"))
                .password(passwordEncoder().encode(body.get("password")))
                .fullName(body.get("fullName"))
                .email(body.get("email"))
                .build();

        return userRepository.save(user);
    }
    public SignInResponse signIn(Map<String, String> body ){
        String username = body.get("username");
        String password = body.get("password");
        String tokenExpo = body.get("tokenExpo");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetails = ((CustomUserDetails) (authentication.getPrincipal()));
        String jwt = tokenProvider.ganerateToken(customUserDetails);
        System.out.println(tokenExpo);
        if(jwt != null && tokenExpo != null){
            TokenExpo expoToken = TokenExpo.builder().username(username).tokenExpo(tokenExpo).build();
            expoTokenRepository.save(expoToken);
        }
        return SignInResponse.builder()
                .refreshToken("")
                .accessToken(jwt)
                .username(customUserDetails.getUsername())
                .email(customUserDetails.getUser().getEmail())
                .fullName(customUserDetails.getUser().getFullName())
                .build();
    }



}
