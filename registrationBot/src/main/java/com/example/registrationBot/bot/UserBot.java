package com.example.registrationBot.bot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class UserBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;
    private final Map<UserState, UserResponseHandler> handlers;
    private final Map<Long, BookingContext> userContexts = new ConcurrentHashMap<>();

    public UserBot(
        @Value("${user.botUsername}") String botUsername,
        @Value("${user.botToken}") String botToken,
        Map<UserState, UserResponseHandler> handlers
    ) {
        super(new DefaultBotOptions(), botToken);
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.handlers = handlers;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        BookingContext context = userContexts.computeIfAbsent(chatId, BookingContext::new);

        // Если команда /start с параметром
        if (text.startsWith("/start")) {
            handlers.get(UserState.START).handle(text, context);
            return;
        }

        // Определяем текущее состояние и вызываем соответствующий handler
        UserState state = context.getState();
        UserResponseHandler handler = handlers.get(state);
        handler.handle(text, context);
    }
    
    public void sendMessage(Long chatId, String text, InlineKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        if (keyboard != null) {
            message.setReplyMarkup(keyboard); // прикрепляем клавиатуру
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    
    public void sendMessage(Long chatId, String text) {
        sendMessage(chatId, text, null); // вызывает второй метод
    }




    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
