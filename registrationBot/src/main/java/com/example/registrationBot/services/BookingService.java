package com.example.registrationBot.services;

import com.example.registrationBot.entities.Admin;
import com.example.registrationBot.entities.Booking;
import com.example.registrationBot.entities.ServiceSlot;
import com.example.registrationBot.mappings.BookingMapper;
import com.example.registrationBot.repositories.AdminRepository;
import com.example.registrationBot.repositories.BookingRepository;
import com.example.registrationBot.repositories.ServiceSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BookingService {

    private final AdminRepository adminRepository;
    private final ServiceSlotRepository serviceSlotRepository;
    private final BookingRepository bookingRepository;

    public BookingService(AdminRepository adminRepository,
                          ServiceSlotRepository serviceSlotRepository,
                          BookingRepository bookingRepository) {
        this.adminRepository = adminRepository;
        this.serviceSlotRepository = serviceSlotRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public void createBooking(Long userTelegramId,
                              String serviceName,
                              String serviceTime,
                              Long adminTelegramId) {
        Admin admin = adminRepository.findByTelegramId(adminTelegramId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));
        ServiceSlot slot = serviceSlotRepository
                .findAllByAdmin_TelegramId(adminTelegramId)
                .stream()
                .filter(s -> s.getName().equals(serviceName) && s.getTime().equals(serviceTime))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ServiceSlot not found"));

        Booking booking = BookingMapper.createBooking(userTelegramId, serviceName, serviceTime, admin, slot);
        bookingRepository.save(booking);
    }

    public List<Booking> getAllBookingsByAdminTelegramId(Long telegramId) {
        return bookingRepository.findAllByAdmin_TelegramId(telegramId);
    }
}
