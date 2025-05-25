package com.example.registrationBot.mappings;

import com.example.registrationBot.entities.Admin;
import com.example.registrationBot.entities.ServiceSlot;
import com.example.registrationBot.utils.TimeParserUtil;
import java.util.ArrayList;
import java.util.List;

public class ServiceSlotMapper {

    public static List<ServiceSlot> createServiceSlots(Admin admin,
                                                       String serviceName,
                                                       String timesString) {
        List<String> times = TimeParserUtil.parseTimesFromString(timesString);
        List<ServiceSlot> slots = new ArrayList<>();
        for (String time : times) {
            ServiceSlot slot = new ServiceSlot();
            slot.setAdmin(admin);
            slot.setName(serviceName);
            slot.setTime(time);
            slots.add(slot);
        }
        return slots;
    }
}
