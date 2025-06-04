package com.example.registrationBot.bot.handlers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import com.example.registrationBot.bot.AdminBot;
import com.example.registrationBot.bot.BookingContext;
import com.example.registrationBot.bot.Controller;
import com.example.registrationBot.bot.UserBot;
import com.example.registrationBot.bot.UserResponseHandler;
import com.example.registrationBot.bot.UserState;
import com.example.registrationBot.utils.KeyboardUtil;

@Component
public class ConfirmationHandler implements UserResponseHandler {
    @Autowired
    private Controller controller;
    @Autowired
    private AdminBot adminBot;

    @Override
    public void handle(String message, BookingContext context, UserBot userBot) {
        String userMsg = message.trim().toLowerCase();
        if (userMsg.equals("нет, отменить запись")) {
            userBot.sendMessage(context.getChatId(),
                "Запись удалена (прототип). Нажмите /start, чтобы начать заново.");
            context.setState(UserState.DONE);
            return;
        }

        // "да, все верно"
        controller.addBooking(context.getChatId(), context.getServiceName(), context.getTime(), context.getAdminId());
        context.setState(UserState.DONE);
        Integer adminId = controller.findIdOfServiceSlot(context.getServiceName(), context.getTime(), context.getAdminId());
        controller.deleteServiceSlot(adminId);
        userBot.sendMessage(context.getChatId(),
            "🎉 Ваша тестовая запись успешно добавлена!\nСпасибо за использование демонстрационного бота.");

        // уведомление админа
        adminBot.sendMessage(context.getAdminId(),
            String.format("Новая запись: %s на %s (демо)",
                context.getServiceName(), context.getTime())
        );
    }

    @Override
    public UserState getState() {
        return UserState.CONFIRMATION;
    }
}