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
import com.example.registrationBot.utils.AdminIdExtractor;
import com.example.registrationBot.utils.KeyboardUtil;

@Component
public class StartHandler implements UserResponseHandler {
	
	@Autowired
    private Controller controller;
   
    @Override
    public void handle(String message, BookingContext context, UserBot userBot) {
    	long adminId = AdminIdExtractor.extractAdminIdFromStartCommand(message);
    	if (!controller.doesServicesExist(adminId)) {
    		userBot.sendMessage(context.getChatId(), "Нету");
    		return;
    	}
    	context.setAdminId(adminId);
    	List<String> services = controller.findAllServicesForUser(adminId, context.getChatId());
    	InlineKeyboardMarkup keyboard = KeyboardUtil.createInlineKeyboard(services);
    	userBot.sendMessage(context.getChatId(), "Услуги: ", keyboard);
    	context.setState(UserState.CHOOSE_SERVICE);
    	
    }

	@Override
	public UserState getState() {
		return UserState.START;
		}
}
