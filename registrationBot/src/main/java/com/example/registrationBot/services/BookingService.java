package com.example.registrationBot.services;

import com.example.registrationBot.entities.Admin;
import com.example.registrationBot.entities.Booking;
import com.example.registrationBot.entities.ClientAdmin;
import com.example.registrationBot.entities.ServiceSlot;
import com.example.registrationBot.mappings.BookingMapper;
import com.example.registrationBot.repositories.AdminRepository;
import com.example.registrationBot.repositories.BookingRepository;
import com.example.registrationBot.repositories.ClientAdminRepository;
import com.example.registrationBot.repositories.ServiceSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BookingService {

    private final AdminRepository adminRepository;
    private final ServiceSlotRepository serviceSlotRepository;
    private final BookingRepository bookingRepository;
    private final ClientAdminRepository clientAdminRepository;

    public BookingService(AdminRepository adminRepository,
                          ServiceSlotRepository serviceSlotRepository,
                          BookingRepository bookingRepository, ClientAdminRepository clientAdminRepository) {
        this.adminRepository = adminRepository;
        this.serviceSlotRepository = serviceSlotRepository;
        this.bookingRepository = bookingRepository;
        this.clientAdminRepository = clientAdminRepository;
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
    
    public Long getAdminIdByClientId(Long clientId) {
        return clientAdminRepository.findByClientId(clientId)
                .map(ClientAdmin::getAdminId)
                .orElse(null);
    }
    
    public void saveClientAdmin(Long clientId, Long adminId) {
        ClientAdmin clientAdmin = new ClientAdmin(clientId, adminId);
        clientAdminRepository.save(clientAdmin);
    }
}
