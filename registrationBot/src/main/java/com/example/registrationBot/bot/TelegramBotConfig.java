package com.example.registrationBot.bot;

import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class TelegramBotConfig {

    private final AdminBot adminBot;

    @Autowired
    public TelegramBotConfig(AdminBot adminBot) {
        this.adminBot = adminBot;
    }

    @PostConstruct
    public void registerBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(adminBot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
