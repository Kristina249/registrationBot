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
    	 Message message = update.getMessage();
         String text = message.getText();
         Long chatId = message.getChatId();
         System.out.println("Апдейт");
         sendMessage(chatId, text);
		/*
		 * System.out.println("Апдейт"); Long chatId = update.getMessage().getChatId();
		 * String text = update.getMessage().getText();
		 * 
		 * BookingContext context = userContexts.get(chatId); if
		 * (update.hasCallbackQuery()) { UserState state = context.getState();
		 * UserResponseHandler handler = handlers.get(state);
		 * 
		 * if (handler != null) { handler.handle(text, context, this); } } if
		 * (!update.hasMessage() || !update.getMessage().hasText()) { // Игнорируем не
		 * текстовые сообщения return; }
		 * 
		 * 
		 * 
		 * // Если сессии нет и пришла команда /start — создаём новую сессию и запускаем
		 * обработчик if (context == null) { System.out.println("Все нормально");
		 * 
		 * if (text.startsWith("/start")) { context = new BookingContext(chatId);
		 * userContexts.put(chatId, context); contextTimestamps.put(chatId,
		 * Instant.now());
		 * 
		 * UserResponseHandler handler = handlers.get(UserState.START); if (handler !=
		 * null) { handler.handle(text, context, this); } else { sendMessage(chatId,
		 * "Извините, бот временно недоступен."); } return; } else { sendMessage(chatId,
		 * "Ваша сессия устарела. Напишите /start, чтобы начать заново."); return; } }
		 * 
		 * // Если сессия есть, обновляем таймстамп contextTimestamps.put(chatId,
		 * Instant.now());
		 * 
		 * // Если пользователь завершил сессию, создаём новую if (context.getState() ==
		 * UserState.DONE) { userContexts.remove(chatId); context = new
		 * BookingContext(chatId); userContexts.put(chatId, context); }
		 */
       
    }

    private void cleanOldContexts() {
        Instant now = Instant.now();
        for (Map.Entry<Long, Instant> entry : contextTimestamps.entrySet()) {
            if (now.minusSeconds(600).isAfter(entry.getValue())) { // 10 минут
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
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
