package com.example.registrationBot.repositories;

import com.example.registrationBot.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByTelegramId(Long telegramId);
}