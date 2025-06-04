package com.example.registrationBot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Component
public class AdminBot extends TelegramLongPollingBot {

    private final Map<Long, ServiceCreationState> serviceCreationStates = new HashMap<>();
    private final String botUsername;
    private final String botToken;
    private final String userBotUsername;

    @Autowired
    private Controller controller;

    public AdminBot(
            @Value("${admin.botUsername}") String botUsername,
            @Value("${admin.botToken}") String botToken,
            @Value("${user.botUsername}")  String userBotUsername) {
        this.botUsername    = botUsername;
        this.botToken       = botToken;
        this.userBotUsername = userBotUsername;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            Long userId = update.getCallbackQuery().getFrom().getId();

            switch (data) {
                case "add_service" -> {
                    serviceCreationStates.put(chatId, new ServiceCreationState());
                    sendMessage(chatId,
                        "📝 Пожалуйста, введите название услуги, которую хотите добавить (демо).\n" +
                        "Например: Массаж, Стрижка, Консультация");
                }
                case "view_services" -> sendMessage(chatId, controller.handleViewAllServices(chatId));
                case "view_bookings" -> sendMessage(chatId, controller.handleViewAllBookings(chatId));
                case "test_client" -> sendClientTestLink(chatId, userId);
                default -> sendAdminOptions(chatId);
            }
            return;
        }

        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        Message message = update.getMessage();
        String text = message.getText();
        Long chatId = message.getChatId();

        if ("/start".equals(text)) {
            // Приветствие через BotFather, код теперь сразу показывает меню
            sendAdminOptions(chatId);
            return;
        }

        if (serviceCreationStates.containsKey(chatId)) {
            ServiceCreationState state = serviceCreationStates.get(chatId);
            if (!state.isNameSet()) {
                state.setName(text);
                sendMessage(chatId,
                    "⏰ Теперь укажите доступное время для этой услуги через запятую.\n" +
                    "Примеры: 10:00, 12:30, 15:00");
            } else {
                controller.handleAddService(chatId, state.getName(), text);
                serviceCreationStates.remove(chatId);
                sendMessage(chatId, "✅ Услуга успешно добавлена (демо).");
                sendAdminOptions(chatId);
            }
        } else {
            sendAdminOptions(chatId);
        }
    }

    private void sendAdminOptions(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Выберите действие:");

        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        rows.add(List.of(
            InlineKeyboardButton.builder()
                .text("➕ Добавить новую услугу")
                .callbackData("add_service")
                .build()
        ));
        rows.add(List.of(
            InlineKeyboardButton.builder()
                .text("📋 Просмотреть все услуги")
                .callbackData("view_services")
                .build()
        ));
        rows.add(List.of(
            InlineKeyboardButton.builder()
                .text("📆 Просмотреть все записи")
                .callbackData("view_bookings")
                .build()
        ));
        rows.add(List.of(
            InlineKeyboardButton.builder()
                .text("🤖 Протестировать как клиент")
                .callbackData("test_client")
                .build()
        ));

        inlineKeyboard.setKeyboard(rows);
        message.setReplyMarkup(inlineKeyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendClientTestLink(Long chatId, Long adminUserId) {
        String link = "https://t.me/" + userBotUsername + "?start=admin_" + adminUserId;
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Откройте бот как клиент (демо):");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(List.of(
            InlineKeyboardButton.builder()
                .text("Запустить клиент-бота")
                .url(link)
                .build()
        )));
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private static class ServiceCreationState {
        private String name;
        public boolean isNameSet() { return name != null; }
        public void setName(String name) { this.name = name; }
        public String getName() { return name; }
    }
}