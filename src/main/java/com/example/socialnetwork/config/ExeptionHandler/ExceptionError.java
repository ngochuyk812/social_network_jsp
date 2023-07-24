package com.example.socialnetwork.config.ExeptionHandler;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ExceptionError {
    public void errorNoToken(HttpServletResponse response, String message){
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/xml; charset=UTF-8");
        response.setStatus(401);
        try {
            response.getWriter().println(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
