package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.Message;
import com.example.socialnetwork.model.Post;
import com.example.socialnetwork.model.Room;
import com.example.socialnetwork.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @EntityGraph(attributePaths = {"mediaPosts", "user"})
    Page<Post> findAll (Pageable pageable);

    @EntityGraph(attributePaths = {"mediaPosts", "user"})
    List<Post> findAll ( );
}