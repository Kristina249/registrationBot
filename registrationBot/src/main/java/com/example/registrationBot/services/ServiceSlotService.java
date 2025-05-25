package com.example.registrationBot.services;

import com.example.registrationBot.entities.Admin;
import com.example.registrationBot.entities.ServiceSlot;
import com.example.registrationBot.mappings.ServiceSlotMapper;
import com.example.registrationBot.repositories.AdminRepository;
import com.example.registrationBot.repositories.ServiceSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ServiceSlotService {

    private final AdminRepository adminRepository;
    private final ServiceSlotRepository serviceSlotRepository;

    public ServiceSlotService(AdminRepository adminRepository,
                              ServiceSlotRepository serviceSlotRepository) {
        this.adminRepository = adminRepository;
        this.serviceSlotRepository = serviceSlotRepository;
    }

    @Transactional
    public void createAndSaveServiceSlots(Long adminTelegramId,
                                          String name,
                                          String times) {
        Admin admin = adminRepository.findByTelegramId(adminTelegramId)
                .orElseGet(() -> {
                    Admin a = new Admin();
                    a.setTelegramId(adminTelegramId);
                    return adminRepository.save(a);
                });

        List<ServiceSlot> slots = ServiceSlotMapper.createServiceSlots(admin, name, times);
        serviceSlotRepository.saveAll(slots);
    }

    public List<ServiceSlot> getServiceSlotsByAdminTelegramId(Long telegramId) {
        return serviceSlotRepository.findAllByAdmin_TelegramId(telegramId);
    }
}