package com.example.registrationBot.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.registrationBot.entities.Booking;
import com.example.registrationBot.entities.ServiceSlot;
import com.example.registrationBot.mappings.ServiceSlotMapper;
import com.example.registrationBot.repositories.BookingRepository;
import com.example.registrationBot.repositories.ServiceSlotRepository;

@Service
public class ServiceSlotService {

    private final ServiceSlotRepository serviceSlotRepository;

    public ServiceSlotService(ServiceSlotRepository serviceSlotRepository) {
        this.serviceSlotRepository = serviceSlotRepository;
    }

    /**
     * 1. Создаёт и сохраняет в базу все слоты для заданного admin-а, имени и списка времён
     */
    public void createAndSaveServiceSlots(Integer telegramId, String name, List<String> times) {
        List<ServiceSlot> slots = ServiceSlotMapper.createServiceSlots(telegramId, name, times);
        serviceSlotRepository.saveAll(slots);
    }

    /**
     * 2. Возвращает все ServiceSlot по telegram_id
     */
    public List<ServiceSlot> getServiceSlotsByTelegramId(long telegramId) {
        return serviceSlotRepository.findAllByTelegramId(telegramId);
    }

    public List<String> getNamesOfServicesByTelegramId(long telegramId) {
    	List<String> names = serviceSlotRepository.findNamesByTelegramId(telegramId);
    	if (names.isEmpty()) {
    		return null;
    	} else {
    		return names;
    	}
    }
}