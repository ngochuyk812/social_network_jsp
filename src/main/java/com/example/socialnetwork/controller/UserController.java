package com.example.socialnetwork.controller;


import com.example.socialnetwork.config.authencation.CustomUserDetails;
import com.example.socialnetwork.config.authencation.JwtTokenProvider;
import com.example.socialnetwork.repository.ExpoTokenRepository;
import com.example.socialnetwork.service.imp.UserServiceImp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@Transactional
@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserServiceImp userService;
    @Autowired
    private ExpoTokenRepository expoTokenRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/sign_up")
    public ResponseEntity<?> sign_up(@RequestParam Map<String, String> body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>(userService.signUp(body), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/sign_in")
    public ResponseEntity<?> sign_in(@RequestParam Map<String, String> body) throws Exception {

        return ResponseEntity.ok().body(userService.signIn(body));
    }
    @PostMapping("/un_register_expo")
    public ResponseEntity<?> unRegisterExpo(@RequestParam Map<String, String> body, @RequestAttribute(name=
            "username") String username) throws Exception {
        String token = body.get("token");
        System.out.println(username);
        if(token != null && username != null){
            expoTokenRepository.deleteByTokenExpoAndUsername(token, username);
            return ResponseEntity.ok().body(token);
        }else{
            return ResponseEntity.badRequest().body("Not authenticated");
        }
    }
}
