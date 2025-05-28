package com.example.registrationBot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.registrationBot.entities.ClientAdmin;

import java.util.Optional;

public interface ClientAdminRepository extends JpaRepository<ClientAdmin, Long> {
    Optional<ClientAdmin> findByClientId(Long clientId);
}
