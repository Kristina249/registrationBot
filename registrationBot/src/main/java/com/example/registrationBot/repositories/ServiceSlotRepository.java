package com.example.registrationBot.repositories;

import com.example.registrationBot.entities.ServiceSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceSlotRepository extends JpaRepository<ServiceSlot, Integer> {
    List<ServiceSlot> findAllByAdmin_TelegramId(Long telegramId);
}
