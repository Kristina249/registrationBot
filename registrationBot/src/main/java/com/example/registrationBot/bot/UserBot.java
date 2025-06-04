package com.example.registrationBot.bot;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class UserBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;
    private final Map<UserState, UserResponseHandler> handlers;
    private final Map<Long, BookingContext> userContexts = new ConcurrentHashMap<>();
    private final Map<Long, Instant> contextTimestamps = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();

    public UserBot(
        @Value("${user.botUsername}") String botUsername,
        @Value("${user.botToken}") String botToken,
        Map<UserState, UserResponseHandler> handlers
    ) {
        super(new DefaultBotOptions(), botToken);
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.handlers = handlers;
        cleaner.scheduleAtFixedRate(this::cleanOldContexts, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            Long chatIdQuery = update.getCallbackQuery().getMessage().getChatId();
            if (!userContexts.containsKey(chatIdQuery)) {
                sendMessage(chatIdQuery, "Ваша сессия истекла. Нажмите /start, чтобы начать заново");
                return;
            }
            if (userContexts.get(chatIdQuery).getState() == UserState.DONE) {
                sendMessage(chatIdQuery, "Если хотите записать еще раз, нажмите /start");
            }
            BookingContext user = userContexts.get(chatIdQuery);
            UserResponseHandler handler = handlers.get(user.getState());
            handler.handle(data, user, this);
            return;
        }

        Message message = update.getMessage();
        if (message == null || !message.hasText()) return;

        String text = message.getText();
        Long chatId = message.getChatId();

        if (text.startsWith("/start")) {
            sendStartMessage(chatId, text);
            return;
        }

        if (userContexts.containsKey(chatId) && userContexts.get(chatId).getState() != UserState.DONE) {
            sendMessage(chatId, "Пожалуйста выберите вариант из списка");
            return;
        }

        if (!userContexts.containsKey(chatId)) {
            sendMessage(chatId, "Ваша сессия истекла. Нажмите /start, чтобы начать заново");
            return;
        }

        if (userContexts.get(chatId).getState() == UserState.DONE) {
            sendStartMessage(chatId, text);
        }
    }

    private void sendStartMessage(long chatId, String text) {
        BookingContext user = new BookingContext(chatId);
        userContexts.put(chatId, user);
        UserResponseHandler handler = handlers.get(user.getState());
        handler.handle(text, user, this);
    }

    private void cleanOldContexts() {
        Instant now = Instant.now();
        for (Map.Entry<Long, Instant> entry : contextTimestamps.entrySet()) {
            if (now.minusSeconds(600).isAfter(entry.getValue())) {
                Long chatId = entry.getKey();
                userContexts.remove(chatId);
                contextTimestamps.remove(chatId);
            }
        }
    }

    public void sendMessage(Long chatId, String text, InlineKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        if (keyboard != null) {
            message.setReplyMarkup(keyboard);
        }
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Long chatId, String text) {
        sendMessage(chatId, text, null);
    }

    @Override
    public String getBotUsername() { return botUsername; }
    @Override
    public String getBotToken() { return botToken; }
}
