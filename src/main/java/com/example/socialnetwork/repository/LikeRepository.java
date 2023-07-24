package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.Like;
import com.example.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {


}