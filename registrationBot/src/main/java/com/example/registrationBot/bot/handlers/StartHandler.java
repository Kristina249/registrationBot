package com.example.registrationBot.bot.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.example.registrationBot.bot.BookingContext;
import com.example.registrationBot.bot.Controller;
import com.example.registrationBot.bot.UserResponseHandler;
import com.example.registrationBot.bot.UserState;
import com.example.registrationBot.utils.AdminIdExtractor;

@Component
public class StartHandler implements UserResponseHandler {
    @Autowired
    @Lazy
	Controller controller;
    @Override
    public void handle(String message, BookingContext context) {
    	long adminId = AdminIdExtractor.extractAdminIdFromStartCommand(message);
    	if (!controller.doesServicesExist(adminId)) {
    		controller.sendMessage(context.getChatId(), "Нету");
    		return;
    	}
    	context.setAdminId(adminId);
    	controller.sendAllServicesToUser(adminId, context.getChatId());
    	context.setState(UserState.CHOOSE_SERVICE);
    	
    }

	@Override
	public UserState getState() {
		return UserState.START;
		}
}
