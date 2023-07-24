package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Query(value = "SELECT r.* FROM room as r JOIN participants as p on r.id = p.room_id where p.username = :username",
            nativeQuery = true)
    List<Room> findRoomsByUsername(@Param("username") String username);}
