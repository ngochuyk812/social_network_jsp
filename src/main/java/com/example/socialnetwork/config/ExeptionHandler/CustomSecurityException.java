package com.example.socialnetwork.config.ExeptionHandler;

public class CustomSecurityException extends SecurityException {

    public CustomSecurityException() {
        super();
    }

    public CustomSecurityException(String message) {
        super(message);
    }

    // Thêm các phương thức tùy chỉnh khác nếu cần

}
