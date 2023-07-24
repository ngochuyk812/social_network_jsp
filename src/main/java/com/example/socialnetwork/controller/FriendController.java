package com.example.socialnetwork.controller;


import com.example.socialnetwork.service.FriendService;
import com.example.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;
    @GetMapping("get_friend")
    public ResponseEntity<?> getFriend(@RequestAttribute("username") String username){

        return ResponseEntity.ok().body(friendService.getFriend(username));
    }
}
