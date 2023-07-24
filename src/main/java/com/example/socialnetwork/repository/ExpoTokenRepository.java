package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.TokenExpo;
import com.example.socialnetwork.model.key.TokenExpoKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface ExpoTokenRepository extends JpaRepository<TokenExpo, TokenExpoKey> {
    void deleteByTokenExpoAndUsername(String token, String username);
    List<TokenExpo> findTokenExposByUsername(String username);
}
