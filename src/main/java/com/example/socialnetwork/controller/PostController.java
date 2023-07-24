package com.example.socialnetwork.controller;


import com.example.socialnetwork.DTO.PostDTO;
import com.example.socialnetwork.DTO.UserDTO;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.payload.request.PostData;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.FriendService;
import com.example.socialnetwork.service.PostService;
import com.example.socialnetwork.utils.AmazonClient;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AmazonClient amazonClient;
    @PostMapping("/upload_post")
    public ResponseEntity<PostDTO> uploadPost(@RequestBody PostData postData,
                                             @RequestAttribute(value = "username") String username) {
        UserDTO user = UserDTO.builder().username(username).build();
        postData.setUserDTO(user);
        PostDTO createPost = postService.uploadPost(postData);
        return ResponseEntity.status(HttpStatus.OK).body(createPost);
    }
    @PostMapping("/init_prisigned_urls")
        public ResponseEntity<?> initPresignedUrls(@RequestParam Map<String, ?> body) throws ParseException {

        String stringExtensions =(String) body.get("extensions");
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(stringExtensions);
        List<String> extensions = new ArrayList<>();
        JSONArray array = (JSONArray)obj;
        System.out.println(body);
        for (Object tmp: array) {
            System.out.println(tmp);
            extensions.add((String) tmp);
        }

        return ResponseEntity.ok().body(postService.initPresignedUrls(extensions));

    }

    @GetMapping("/get_all")
    public ResponseEntity<?> GetPosts(){
        return ResponseEntity.ok().body(postService.getAllPost());

    }

    @GetMapping("/")
    public ResponseEntity<?> TestError(){
        return ResponseEntity.ok().body("Errror");

    }
}
