package com.example.socialnetwork.service.imp;

import com.amazonaws.HttpMethod;
import com.example.socialnetwork.DTO.PostDTO;
import com.example.socialnetwork.DTO.UserDTO;
import com.example.socialnetwork.model.Friend;
import com.example.socialnetwork.model.MediaPost;
import com.example.socialnetwork.model.Post;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.payload.request.MediaPostData;
import com.example.socialnetwork.payload.request.PostData;
import com.example.socialnetwork.repository.FriendRepository;
import com.example.socialnetwork.repository.MediaPostRepository;
import com.example.socialnetwork.repository.PostRepository;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.FriendService;
import com.example.socialnetwork.service.PostService;
import com.example.socialnetwork.service.UploadAwsService;
import com.example.socialnetwork.utils.AmazonClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class PostServiceImp implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UploadAwsService uploadAwsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MediaPostRepository mediaPostRepository;
    @Override
    public List<String> initPresignedUrls(List<String> expe) {
        return expe.stream().map(tmp->{
            return uploadAwsService.generateUrlPublic(uploadAwsService.ganerateNameRandom(tmp), HttpMethod.PUT);
        }).toList();
    }

    @Override
    public PostDTO uploadPost(PostData postData) {
        User user = userRepository.findByUsername(postData.getUserDTO().getUsername());

        Post post = Post.builder()
                .caption(postData.getCaption())
                .audience(postData.getAudience())
                .layout(postData.getLayout())
                .user(user)
                .build();
        post = postRepository.save(post);
        List<MediaPostData> medias = postData.getMedias();
        List<MediaPost> mediaPosts = new ArrayList<>();
        for (MediaPostData tmp: medias) {
            mediaPosts.add(MediaPost.builder()
                    .post(post)
                    .src(tmp.getSrc())
                    .type(tmp.getType()).build());
        }

        mediaPosts = mediaPostRepository.saveAll(mediaPosts);
        post.setMediaPosts(mediaPosts);


        return post.toDTO();
    }
    public List<PostDTO> getAllPost(){
        List<Post> postDTOS = postRepository.findAll();
        return postDTOS.stream().map(tmp->tmp.toDTO()).toList();
    }
}
