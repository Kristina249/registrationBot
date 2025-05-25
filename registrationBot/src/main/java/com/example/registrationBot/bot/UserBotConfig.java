package com.example.registrationBot.bot;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class UserBotConfig {

    private final Map<UserState, UserResponseHandler> handlers;

    public UserBotConfig(Map<UserState, UserResponseHandler> handlers) {
        this.handlers = handlers;
    }

    @Value("${user.botUsername}")
    private String botUsername;

    @Value("${user.botToken}")
    private String botToken;

    @Bean
    public UserBot userBot() {
        return new UserBot(botUsername, botToken, handlers);
    }
}