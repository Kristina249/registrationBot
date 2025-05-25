package com.example.registrationBot.bot.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.example.registrationBot.bot.BookingContext;
import com.example.registrationBot.bot.Controller;
import com.example.registrationBot.bot.UserResponseHandler;
import com.example.registrationBot.bot.UserState;

@Component
public class ChooserServiceHandler implements UserResponseHandler {

    @Autowired
    @Lazy
    private Controller controller;

    @Override
    public void handle(String message, BookingContext context) {
        Long adminId = context.getAdminId();
        Long chatId = context.getChatId();

        if (!controller.doesServicesExist(adminId)) {
            controller.sendMessage(chatId, "Пожалуйста, выберите услугу из списка ниже:");
            controller.sendAllServicesToUser(adminId, chatId);
            return;
        }

        context.setServiceName(message);
        controller.sendAllTimesOfServicesToUser(adminId, chatId, message);
        context.setState(UserState.CHOOSE_TIME);
    }

    @Override
    public UserState getState() {
        return UserState.CHOOSE_SERVICE;
    }
}
