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
public class ChooseTimeHandler implements UserResponseHandler {
    @Override
    public void handle(String message, BookingContext context, UserBot userBot) {
        context.setTime(message);
        InlineKeyboardMarkup keyboard = KeyboardUtil.createInlineKeyboard(
            List.of("Да, все верно", "Нет, отменить запись")
        );
        userBot.sendMessage(context.getChatId(),
            String.format("✅ Вы выбрали:\nУслуга: %s\nВремя: %s\nПодтвердить запись?",
                context.getServiceName(), context.getTime()), keyboard);

        context.setState(UserState.CONFIRMATION);
    }

    @Override
    public UserState getState() {
        return UserState.CHOOSE_TIME;
    }
}
