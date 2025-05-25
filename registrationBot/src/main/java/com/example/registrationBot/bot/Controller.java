package com.example.registrationBot.bot;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import com.example.registrationBot.entities.Booking;
import com.example.registrationBot.entities.ServiceSlot;
import com.example.registrationBot.services.BookingService;
import com.example.registrationBot.services.ServiceSlotService;
import com.example.registrationBot.utils.KeyboardUtil;

@Service
public class Controller {

    private final ServiceSlotService serviceSlotService;
    private final BookingService bookingService;
    private final UserBot userBot;

    public Controller(ServiceSlotService serviceSlotService,
                      BookingService bookingService,
                      UserBot userBot) {
        this.serviceSlotService = serviceSlotService;
        this.bookingService = bookingService;
        this.userBot = userBot;
    }

    public void sendMessage(long chatId, String message) {
        userBot.sendMessage(chatId, message);
    }

    public void handleViewAllBookings(long adminTelegramId) {
        List<Booking> bookings = bookingService.getAllBookingsByAdminTelegramId(adminTelegramId);
        if (bookings.isEmpty()) {
            userBot.sendMessage(adminTelegramId, "Нет записей.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Booking booking : bookings) {
            sb.append(booking.getName())
              .append(" - ")
              .append(booking.getTime())
              .append("\n");
        }
        userBot.sendMessage(adminTelegramId, sb.toString());
    }

    public void handleViewAllServices(long adminTelegramId) {
        List<ServiceSlot> slots = serviceSlotService.getServiceSlotsByAdminTelegramId(adminTelegramId);
        if (slots.isEmpty()) {
            userBot.sendMessage(adminTelegramId, "Список услуг пуст.");
            return;
        }
        Map<String, List<ServiceSlot>> grouped = slots.stream()
                .collect(Collectors.groupingBy(ServiceSlot::getName));
        StringBuilder sb = new StringBuilder();
        grouped.forEach((name, list) -> {
            sb.append(name).append(": ");
            String times = list.stream()
                    .map(ServiceSlot::getTime)
                    .collect(Collectors.joining(", "));
            sb.append(times).append("\n");
        });
        userBot.sendMessage(adminTelegramId, sb.toString());
    }

    public void handleAddService(long adminTelegramId, String name, String time) {
        serviceSlotService.createAndSaveServiceSlots(adminTelegramId, name, time);
    }

    public void sendAllServicesToUser(long adminId, long chatId) {
        List<ServiceSlot> slots = serviceSlotService.getServiceSlotsByAdminTelegramId(adminId);
        List<String> uniqueNames = slots.stream()
                .map(ServiceSlot::getName)
                .distinct()
                .collect(Collectors.toList());
        InlineKeyboardMarkup keyboard = KeyboardUtil.createInlineKeyboard(uniqueNames);
        userBot.sendMessage(chatId, "Услуги:", keyboard);
    }

    public void sendAllTimesOfServicesToUser(long adminId, long chatId, String serviceName) {
        List<ServiceSlot> slots = serviceSlotService.getServiceSlotsByAdminTelegramId(adminId);
        List<String> times = slots.stream()
                .filter(s -> s.getName().equals(serviceName))
                .map(ServiceSlot::getTime)
                .distinct()
                .collect(Collectors.toList());
        InlineKeyboardMarkup keyboard = KeyboardUtil.createInlineKeyboard(times);
        userBot.sendMessage(chatId, "Время:", keyboard);
    }

    public void checkInformation(long chatId, String serviceName, String time) {
        userBot.sendMessage(chatId,
                String.format("Услуга: %s Время: %s. Напиши да/нет", serviceName, time));
    }

    public boolean doesServicesExist(long adminId) {
        return !serviceSlotService.getServiceSlotsByAdminTelegramId(adminId).isEmpty();
    }

    public boolean doesTimesExist(long adminId, String serviceName) {
        return serviceSlotService.getServiceSlotsByAdminTelegramId(adminId).stream()
                .anyMatch(s -> s.getName().equals(serviceName));
    }

    public void addBooking(long userTelegramId, String serviceName, String time, long adminTelegramId) {
        bookingService.createBooking(userTelegramId, serviceName, time, adminTelegramId);
        userBot.sendMessage(userTelegramId, "Запись успешно добавлена.");
    }
}
