package com.example.registrationBot.repositories;

import com.example.registrationBot.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByAdmin_TelegramId(Long telegramId);
}
