package com.example.registrationBot.bot;

public interface UserResponseHandler {
    void handle(String message, BookingContext context, UserBot userBot);
    UserState getState();
}