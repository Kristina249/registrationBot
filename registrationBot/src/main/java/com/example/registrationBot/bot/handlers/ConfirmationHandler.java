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
        Long chatId = context.getChatId();
        Long adminId = context.getAdminId();
        String serviceName = context.getServiceName();
        String time = context.getTime();

        String userMessage = message.trim().toLowerCase();

        if (!userMessage.equals("да, все верно") && !userMessage.equals("нет, отменить запись")) {
            userBot.sendMessage(chatId, "Пожалуйста, ответьте 'да' или 'нет'");
            InlineKeyboardMarkup keyboard = KeyboardUtil.createInlineKeyboard(List.of("Да, все верно", "Нет, отменить запись"));
            userBot.sendMessage(chatId, "Услуга: " + context.getServiceName() + " Время: " + context.getTime() + " Все верно?", keyboard);            return;
        }

        if (userMessage.equals("нет, отменить запись")) {
            userBot.sendMessage(chatId, "Запись удалена.");
            context.setState(UserState.DONE);
            return;
        }

        // Ответ "да"
        Integer id = controller.findIdOfServiceSlot(serviceName, time, adminId);
        if (id != null) {
            controller.addBooking(chatId, serviceName, time, adminId);
            controller.deleteServiceSlot(id);
            context.setState(UserState.DONE);
            userBot.sendMessage(chatId, "Записались");
            adminBot.sendMessage(context.getAdminId(), "Новая запись: " + context.getServiceName() + " на " + context.getTime());
        } else {
        	userBot.sendMessage(chatId, "Запись не найдена");
        	return;
        }
 
    }

    @Override
    public UserState getState() {
        return UserState.CONFIRMATION;
    }
}
