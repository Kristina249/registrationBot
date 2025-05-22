package com.example.registrationBot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.registrationBot.entities.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    // Найти все записи по admin_telegram_id
    List<Booking> findAllByAdminTelegramId(long adminTelegramId);
   
    

}