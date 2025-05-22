package com.example.registrationBot.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "telegram_id")
    private long telegramId;

    @Column(name = "name")
    private String name;

    @Column(name = "time")
    private String time;

    @ManyToOne
    @JoinColumn(name = "admin_telegram_id", referencedColumnName = "telegram_id")
    private ServiceSlot adminId;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public long getTelegramId() { return telegramId; }
    public void setTelegramId(long telegramId) { this.telegramId = telegramId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public ServiceSlot getAdmin() { return adminId; }
    public void setAdmin(ServiceSlot adminId) { this.adminId = adminId; }
}
