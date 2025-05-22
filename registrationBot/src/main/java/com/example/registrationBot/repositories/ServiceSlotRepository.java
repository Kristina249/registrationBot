package com.example.registrationBot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.registrationBot.entities.ServiceSlot;

import java.util.List;

@Repository
public interface ServiceSlotRepository extends JpaRepository<ServiceSlot, Integer> {

    // Найти все записи по telegramId
    List<ServiceSlot> findAllByTelegramId(long telegramId);
    
    @Query("SELECT s.name FROM ServiceSlot s WHERE s.telegramId = :telegramId")
    List<String> findNamesByTelegramId(@Param("telegramId") long telegramId);
}