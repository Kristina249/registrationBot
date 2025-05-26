package com.example.registrationBot.bot;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
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
    
    @Bean
    public TelegramBotsApi telegramBotsApi(List<TelegramLongPollingBot> bots) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        for (TelegramLongPollingBot bot : bots) {
            api.registerBot(bot);
        }
        return api;
    }
}