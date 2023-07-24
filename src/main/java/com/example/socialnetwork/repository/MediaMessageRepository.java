package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.MediaMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface MediaMessageRepository extends JpaRepository<MediaMessage, Integer> {
}
