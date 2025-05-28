package com.example.registrationBot.bot;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.registrationBot.entities.Booking;
import com.example.registrationBot.entities.ServiceSlot;
import com.example.registrationBot.services.BookingService;
import com.example.registrationBot.services.ServiceSlotService;

@Service
public class Controller {

    @Autowired
    private ServiceSlotService serviceSlotService;

    @Autowired
    private BookingService bookingService;

    public String handleViewAllBookings(long adminTelegramId) {
        List<Booking> bookings = bookingService.getAllBookingsByAdminTelegramId(adminTelegramId);
        if (bookings.isEmpty()) {
            return "Нет записей.";
        }
        StringBuilder sb = new StringBuilder();
        for (Booking booking : bookings) {
            sb.append(booking.getName())
              .append(" - ")
              .append(booking.getTime())
              .append("\n");
        }
        return sb.toString();
    }

    public String handleViewAllServices(long adminTelegramId) {
        List<ServiceSlot> slots = serviceSlotService.getServiceSlotsByAdminTelegramId(adminTelegramId);
        if (slots.isEmpty()) {
            return "Список услуг пуст.";
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
        return sb.toString();
    }

    public void handleAddService(long adminTelegramId, String name, String time) {
        serviceSlotService.createAndSaveServiceSlots(adminTelegramId, name, time);
    }

    public List<String> findAllServicesForUser(long adminId, long chatId) {
        List<ServiceSlot> slots = serviceSlotService.getServiceSlotsByAdminTelegramId(adminId);
        return slots.stream()
                .map(ServiceSlot::getName)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> findAllTimesOfServicesForUser(long adminId, long chatId, String serviceName) {
        List<ServiceSlot> slots = serviceSlotService.getServiceSlotsByAdminTelegramId(adminId);
        return slots.stream()
                .filter(s -> s.getName().equals(serviceName))
                .map(ServiceSlot::getTime)
                .distinct()
                .collect(Collectors.toList());
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
    }
    public Long getAdminIdFromAdminClient(long clientId) {
    	Long adminId = bookingService.getAdminIdByClientId(clientId);
    	if (adminId == null) {
    		return null;
    	} else {
    		return adminId;
    	}
    }
  
    public void addClientAdmin(Long clientId, Long adminId) {
    	bookingService.saveClientAdmin(clientId, adminId);
    }
    
    public void deleteServiceSlot(int id) {
    	serviceSlotService.deleteServiceSlotById(id);
    }
    public Integer findIdOfServiceSlot(String name, String time, Long adminTelegramId) {
    	if (serviceSlotService.findServiceSlotId(name, time, adminTelegramId) == null) {
    		return 0;
    	} else {
    		return serviceSlotService.findServiceSlotId(name, time, adminTelegramId);
    	}
    	
    }
}
