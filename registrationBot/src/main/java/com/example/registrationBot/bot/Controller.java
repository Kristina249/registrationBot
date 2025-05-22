package com.example.registrationBot.bot;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import com.example.registrationBot.repositories.BookingRepository;
import com.example.registrationBot.repositories.ServiceSlotRepository;
import com.example.registrationBot.services.ServiceSlotService;
import com.example.registrationBot.utils.KeyboardUtil;

public class Controller {
	ServiceSlotRepository serviceRepository;
	ServiceSlotService service = new ServiceSlotService(serviceRepository);
	private UserBot userBot;
	public void handleAddServiceName(String chatId) {
		
	}
	public void handleViewAllServices(long chatId) {
		
	}
	public void handleViewAllBookings(long chatId) {
		
	}
	public void handleAddService(long chatId, String name, String time) {
		
	}
	public void sendAllServicesToUser(long telegramId, long chatId) {
		List<String> names = service.getNamesOfServicesByTelegramId(telegramId);
		if (names == null) {
			userBot.sendMessage(chatId, "Нету");
			return;
		}
		List<String> uniqueNames = new ArrayList<>();
		for (String name: names) {
			if (!(uniqueNames.contains(name))) {
				uniqueNames.add(name);
			}
		}
		InlineKeyboardMarkup keyboard = KeyboardUtil.createInlineKeyboard(uniqueNames);
		userBot.sendMessage(chatId, "Услуги: ", keyboard);
	}
	
	
}
