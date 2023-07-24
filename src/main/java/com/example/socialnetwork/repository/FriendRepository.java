package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.Friend;
import com.example.socialnetwork.model.TokenExpo;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.model.key.FriendKey;
import com.example.socialnetwork.model.key.TokenExpoKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface FriendRepository extends JpaRepository<Friend, FriendKey> {
    List<Friend> findAllByUsernameOneOrUsernameTwo(User user1, User user2);

    @Query( value =  "SELECT * FROM `friend` where (username_one = :username1 and username_two = :username2" +
            " or " +
            "(username_one = :username2 and username_two = :username1)",
            nativeQuery = true)
    List<User> checkByFriend(@Param("username1") String username1,@Param("username2") String username2);
}
