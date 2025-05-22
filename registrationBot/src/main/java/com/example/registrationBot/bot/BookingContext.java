package com.example.registrationBot.bot;

public class BookingContext {
    private Long adminId;
    private Long chatId;
    private String serviceName;
    private String time;
    private UserState state;

    public BookingContext(Long chatId) {
        this.chatId = chatId;
        this.state = UserState.START;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getUserId() {
        return chatId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }
}
