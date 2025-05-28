package com.example.registrationBot.repositories;

import com.example.registrationBot.entities.Admin;
import com.example.registrationBot.entities.ServiceSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ServiceSlotRepository extends JpaRepository<ServiceSlot, Integer> {
    List<ServiceSlot> findAllByAdmin_TelegramId(Long telegramId);
    Optional<ServiceSlot> findByNameAndTimeAndAdmin(String name, String time, Admin admin);

}
