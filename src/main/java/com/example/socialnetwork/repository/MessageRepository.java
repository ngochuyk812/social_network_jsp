package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.Message;
import com.example.socialnetwork.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    Slice<Message> findFirstNumberByRoomOrderByCreateAtDesc(Room room, Pageable pageable);
    Page<Message> findAllByRoom (Room room, Pageable pageable);

}
