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
    
    @Autowired
    private Controller controller;

    public AdminBot(@Value("${admin.botUsername}") String botUsername,
                    @Value("${admin.botToken}") String botToken) {
        this.botUsername = botUsername;
        this.botToken = botToken;
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
                    sendMessage(chatId, "Введите название новой услуги:");
                }
                case "view_services" -> {
                    String text = controller.handleViewAllServices(chatId);
                    sendMessage(chatId, text);
                }
                case "view_bookings" -> {
                    String text = controller.handleViewAllBookings(chatId);
                    sendMessage(chatId, text);
                }
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
            sendAdminOptions(chatId);
            return;
        }

        if (serviceCreationStates.containsKey(chatId)) {
            ServiceCreationState state = serviceCreationStates.get(chatId);
            if (!state.isNameSet()) {
                state.setName(text);
                sendMessage(chatId, "Теперь введите время для этой услуги:");
            } else {
                String name = state.getName();
                String time = text;
                controller.handleAddService(chatId, name, time);
                serviceCreationStates.remove(chatId);
                sendMessage(chatId, "Услуга успешно добавлена.");
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

        rows.add(List.of(InlineKeyboardButton.builder()
                .text("\u2795 Добавить новую услугу")
                .callbackData("add_service")
                .build()));

        rows.add(List.of(InlineKeyboardButton.builder()
                .text("\uD83D\uDCCB Просмотреть все услуги")
                .callbackData("view_services")
                .build()));

        rows.add(List.of(InlineKeyboardButton.builder()
                .text("\uD83D\uDCC6 Просмотреть все записи")
                .callbackData("view_bookings")
                .build()));

        rows.add(List.of(InlineKeyboardButton.builder()
                .text("\uD83E\uDDEA Протестировать как клиент")
                .callbackData("test_client")
                .build()));

        inlineKeyboard.setKeyboard(rows);
        message.setReplyMarkup(inlineKeyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendClientTestLink(Long chatId, Long adminUserId) {
        String link = "https://t.me/" + botUsername + "?start=admin_" + adminUserId;
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Откройте бота как клиент: " + link);

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
