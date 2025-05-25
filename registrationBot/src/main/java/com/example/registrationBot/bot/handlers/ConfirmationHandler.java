package com.example.registrationBot.bot.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.example.registrationBot.bot.BookingContext;
import com.example.registrationBot.bot.Controller;
import com.example.registrationBot.bot.UserResponseHandler;
import com.example.registrationBot.bot.UserState;

@Component
public class ConfirmationHandler implements UserResponseHandler {

    @Autowired
    @Lazy
    private Controller controller;

    @Override
    public void handle(String message, BookingContext context) {
        Long chatId = context.getChatId();
        Long adminId = context.getAdminId();
        String serviceName = context.getServiceName();
        String time = context.getTime();

        String userMessage = message.trim().toLowerCase();

        if (!userMessage.equals("да") && !userMessage.equals("нет")) {
            controller.sendMessage(chatId, "Пожалуйста, ответьте 'да' или 'нет'");
            controller.checkInformation(chatId, serviceName, time);
            return;
        }

        if (userMessage.equals("нет")) {
            controller.sendMessage(chatId, "Запись удалена.");
            context.setState(UserState.DONE);
            return;
        }

        // Ответ "да"
        controller.addBooking(chatId, serviceName, time, adminId);
        context.setState(UserState.DONE);
    }

    @Override
    public UserState getState() {
        return UserState.CONFIRMATION;
    }
}
