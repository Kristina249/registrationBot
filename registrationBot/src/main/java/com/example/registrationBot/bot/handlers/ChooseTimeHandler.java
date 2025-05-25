package com.example.registrationBot.bot.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.example.registrationBot.bot.BookingContext;
import com.example.registrationBot.bot.Controller;
import com.example.registrationBot.bot.UserResponseHandler;
import com.example.registrationBot.bot.UserState;

@Component
public class ChooseTimeHandler implements UserResponseHandler {

    @Autowired
    @Lazy
    private Controller controller;

    @Override
    public void handle(String message, BookingContext context) {
        Long adminId = context.getAdminId();
        Long chatId = context.getChatId();
        String serviceName = context.getServiceName();

        if (!controller.doesTimesExist(adminId, serviceName)) {
            controller.sendMessage(chatId, "Пожалуйста, выберите время из списка:");
            controller.sendAllTimesOfServicesToUser(adminId, chatId, serviceName);
            return;
        }

        context.setTime(message);
        controller.checkInformation(chatId, serviceName, message);
        context.setState(UserState.CONFIRMATION);
    }

    @Override
    public UserState getState() {
        return UserState.CHOOSE_TIME;
    }
}
