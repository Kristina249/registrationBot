package com.example.registrationBot.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardUtil {

    public static InlineKeyboardMarkup createInlineKeyboard(List<String> options) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (String option : options) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(option);
            button.setCallbackData(option); // можно указать любое значение, по которому потом обрабатываешь

            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(button);
            keyboard.add(row); // каждая кнопка в своей строке
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }
}
