package com.example.registrationBot.bot.handlers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import com.example.registrationBot.bot.BookingContext;
import com.example.registrationBot.bot.Controller;
import com.example.registrationBot.bot.UserBot;
import com.example.registrationBot.bot.UserResponseHandler;
import com.example.registrationBot.bot.UserState;
import com.example.registrationBot.utils.KeyboardUtil;
import com.example.registrationBot.utils.AdminIdExtractor;

@Component
public class StartHandler implements UserResponseHandler {
    @Autowired
    private Controller controller;

    @Override
    public void handle(String message, BookingContext context, UserBot userBot) {
        long adminId;
        if (controller.getAdminIdFromAdminClient(context.getChatId()) == null) {
            adminId = AdminIdExtractor.extractAdminIdFromStartCommand(message);
            controller.addClientAdmin(context.getChatId(), adminId);
        } else {
            adminId = controller.getAdminIdFromAdminClient(context.getChatId());
        }

        context.setAdminId(adminId);

        userBot.sendMessage(context.getChatId(),
            "👋 Привет! Ты попал в demo-бот для записи на услуги.\n" +
            "Этот бот создан в учебных целях — ни одна из услуг не является реальной.\n" +
            "Нажми /start, чтобы продолжить.");

        List<String> services = controller.findAllServicesForUser(adminId, context.getChatId());
        InlineKeyboardMarkup keyboard = KeyboardUtil.createInlineKeyboard(services);
        userBot.sendMessage(context.getChatId(),
            "💼 Выберите услугу, на которую хотите записаться:", keyboard);

        context.setState(UserState.CHOOSE_SERVICE);
    }

    @Override
    public UserState getState() {
        return UserState.START;
    }
}
