package com.example.registrationBot.utils;

public class AdminIdExtractor {
	public static Long extractAdminIdFromStartCommand(String text) {
	    if (text == null) {
	        return null;
	    }
	    // Обрезаем префикс "/start"
	    String payload = text.startsWith("/start") 
	        ? text.substring(6).trim() 
	        : "";
	    // Ожидаем, что payload начинается с "admin_"
	    if (payload.startsWith("admin_")) {
	        String idPart = payload.substring("admin_".length());
	        try {
	            return Long.parseLong(idPart);
	        } catch (NumberFormatException e) {
	            // payload не число — возвращаем null
	        }
	    }
	    return null;
	}
}
