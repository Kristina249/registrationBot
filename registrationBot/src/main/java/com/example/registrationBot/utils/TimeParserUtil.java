package com.example.registrationBot.utils;

import java.util.Arrays;
import java.util.List;

public class TimeParserUtil {
    public static List<String> parseTimesFromString(String timeString) {
        if (timeString == null || timeString.isBlank()) {
            return List.of();
        }
        return Arrays.stream(timeString.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}