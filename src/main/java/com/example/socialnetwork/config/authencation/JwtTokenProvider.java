package com.example.socialnetwork.config.authencation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.Date;


@Slf4j
@Component
public class JwtTokenProvider {
    @Value("${JWT_SECRET}")
    private String JWT_SECRET ;
    private long JWT_EXPIRATION = 604800000;

    public String ganerateToken(CustomUserDetails userDetails){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
    }

    public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token){
        Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
        return true;

    }

    public static void main(String[] args) {
        Date now = new Date();
        System.out.println(new JwtTokenProvider().getUsernameFromJWT("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZ29jaHV5IiwiaWF0IjoxNjg4ODA1OTA2LCJleHAiOjE2ODk0MTA3MDZ9.WIZ3CpILfVk4yQp7Nhnphk0WjLd79lFGdoUsbWyRS_0"));

    }
}
