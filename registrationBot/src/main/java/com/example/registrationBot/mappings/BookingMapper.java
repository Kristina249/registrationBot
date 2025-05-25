package com.example.registrationBot.mappings;

import com.example.registrationBot.entities.Admin;
import com.example.registrationBot.entities.Booking;
import com.example.registrationBot.entities.ServiceSlot;

public class BookingMapper {

    public static Booking createBooking(Long userTelegramId,
                                        String serviceName,
                                        String serviceTime,
                                        Admin admin,
                                        ServiceSlot serviceSlot) {
        Booking booking = new Booking();
        booking.setTelegramId(userTelegramId);
        booking.setName(serviceName);
        booking.setTime(serviceTime);
        booking.setAdmin(admin);
        booking.setServiceSlot(serviceSlot);
        return booking;
    }
}