package com.example.registrationBot.bot.handlers;

import org.springframework.stereotype.Component;

import com.example.registrationBot.bot.BookingContext;
import com.example.registrationBot.bot.Controller;
import com.example.registrationBot.bot.UserResponseHandler;
import com.example.registrationBot.bot.UserState;
import com.example.registrationBot.utils.AdminIdExtractor;

@Component
public class StartHandler implements UserResponseHandler {
    @Override
    public void handle(String message, BookingContext context) {
    	long adminId = AdminIdExtractor.extractAdminIdFromStartCommand(message);
    	context.setAdminId();
    	Controller controller = new Controller();
    	controller.sendAllServicesToUser();
    	
    }

	@Override
	public UserState getState() {
		return UserState.START;
		}
}
