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
	
	@Autowired
    private Controller controller;
	
    @Override
    public void handle(String message, BookingContext context, UserBot userBot) {
        Long adminId = context.getAdminId();
        Long chatId = context.getChatId();
        String serviceName = context.getServiceName();

        if (!controller.doesTimesExist(adminId, serviceName)) {
            userBot.sendMessage(chatId, "Пожалуйста, выберите время из списка:");
            List<String> times = controller.findAllTimesOfServicesForUser(adminId, chatId, message);
            InlineKeyboardMarkup keyboard = KeyboardUtil.createInlineKeyboard(times);
            userBot.sendMessage(chatId, "Время: ", keyboard);            return;
        }

        context.setTime(message);
        InlineKeyboardMarkup keyboard = KeyboardUtil.createInlineKeyboard(List.of("Да, все верно", "Нет, отменить запись"));
        userBot.sendMessage(chatId, "Услуга: " + context.getServiceName() + " Время: " + context.getTime() + " Все верно?", keyboard);
        context.setState(UserState.CONFIRMATION);
    }

    @Override
    public UserState getState() {
        return UserState.CHOOSE_TIME;
    }
}
