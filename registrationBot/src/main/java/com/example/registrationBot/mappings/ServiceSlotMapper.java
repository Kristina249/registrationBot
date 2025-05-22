package com.example.registrationBot.mappings;


import java.util.ArrayList;
import java.util.List;

import com.example.registrationBot.entities.ServiceSlot;

public class ServiceSlotMapper {

    public static List<ServiceSlot> createServiceSlots(Integer telegramId, String name, List<String> times) {
        List<ServiceSlot> slots = new ArrayList<>();
        for (String time : times) {
            ServiceSlot slot = new ServiceSlot();
            slot.setTelegramId(telegramId);
            slot.setName(name);
            slot.setTime(time);
            slots.add(slot);
        }
        return slots;
    }
}
