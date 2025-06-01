package com.example.registrationBot.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "time", nullable = false)
    private String time;

    // Привязка к таблице admins по telegram_id администратора
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "telegram_id", referencedColumnName = "telegram_id", nullable = false)
    private Admin admin;
    
    @Column(name = "userId", nullable = false)
    private Long userId;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    
    public Long getUserId() {
    	return userId;
    }
    public void setUserId(Long userId) {
    	this.userId = userId;
    }
}
