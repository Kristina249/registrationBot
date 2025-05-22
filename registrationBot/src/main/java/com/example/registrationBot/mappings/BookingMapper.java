package com.example.registrationBot.mappings;

import com.example.registrationBot.entities.Booking;
import com.example.registrationBot.entities.ServiceSlot;

public class BookingMapper {

    public static Booking createBooking(Integer telegramId, String name, String time, ServiceSlot adminTelegramId) {
        Booking booking = new Booking();
        booking.setTelegramId(telegramId);
        booking.setName(name);
        booking.setTime(time);
        booking.setAdmin(adminTelegramId);
        return booking;
    }
}