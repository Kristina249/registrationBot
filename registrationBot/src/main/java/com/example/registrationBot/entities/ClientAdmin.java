package com.example.registrationBot.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "client_admin")
public class ClientAdmin {

    @Id
    private Long clientId;

    private Long adminId;

    // Конструкторы
    public ClientAdmin() {
    }

    public ClientAdmin(Long clientId, Long adminId) {
        this.clientId = clientId;
        this.adminId = adminId;
    }

    // Геттеры и сеттеры
    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}
