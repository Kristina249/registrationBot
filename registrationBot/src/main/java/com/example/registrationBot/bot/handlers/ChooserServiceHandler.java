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

@Component
public class ChooserServiceHandler implements UserResponseHandler {
    @Autowired
    private Controller controller;

    @Override
    public void handle(String message, BookingContext context, UserBot userBot) {
        Long adminId = context.getAdminId();
        Long chatId = context.getChatId();

        context.setServiceName(message);
        List<String> times = controller.findAllTimesOfServicesForUser(adminId, chatId, message);
        InlineKeyboardMarkup keyboard = KeyboardUtil.createInlineKeyboard(times);
        userBot.sendMessage(chatId,
            "⏰ Отлично! Теперь выберите удобное время из доступных вариантов:", keyboard);

        context.setState(UserState.CHOOSE_TIME);
    }

    @Override
    public UserState getState() {
        return UserState.CHOOSE_SERVICE;
    }
}
