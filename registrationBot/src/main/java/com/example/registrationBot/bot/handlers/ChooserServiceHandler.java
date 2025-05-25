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
        if (!controller.doesServicesExist(adminId)) {
        	userBot.sendMessage(chatId, "Пожалуйста, выберите услугу из списка ниже:");
            List<String> services = controller.findAllServicesForUser(adminId, chatId);
            InlineKeyboardMarkup keyboard = KeyboardUtil.createInlineKeyboard(services);
        	userBot.sendMessage(context.getChatId(), "Услуги: ", keyboard);
            return;
        }

        context.setServiceName(message);
        List<String> times = controller.findAllTimesOfServicesForUser(adminId, chatId, message);
        InlineKeyboardMarkup keyboard = KeyboardUtil.createInlineKeyboard(times);
        userBot.sendMessage(chatId, "Время: ", keyboard);
        context.setState(UserState.CHOOSE_TIME);
    }

    @Override
    public UserState getState() {
        return UserState.CHOOSE_SERVICE;
    }
}
