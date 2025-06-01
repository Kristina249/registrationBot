package com.example.registrationBot.mappings;

import com.example.registrationBot.entities.Admin;
import com.example.registrationBot.entities.Booking;
import com.example.registrationBot.entities.ServiceSlot;

public class BookingMapper {

    public static Booking createBooking(Long userTelegramId,
                                        String serviceName,
                                        String serviceTime,
                                        Admin admin) {
        Booking booking = new Booking();
        booking.setUserId(userTelegramId);
        booking.setName(serviceName);
        booking.setTime(serviceTime);
        booking.setAdmin(admin);
        return booking;
    }
}