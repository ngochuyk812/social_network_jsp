package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.Participant;
import com.example.socialnetwork.model.Room;
import com.example.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    ArrayList<Participant> findAllByRoom(Room room);
}
