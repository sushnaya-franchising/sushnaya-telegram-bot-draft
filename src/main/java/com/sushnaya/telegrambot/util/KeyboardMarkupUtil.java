package com.sushnaya.telegrambot.util;

import com.google.common.collect.Lists;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class KeyboardMarkupUtil {
    private KeyboardMarkupUtil() {
    }

    public static InlineKeyboardMarkup insertButton(InlineKeyboardMarkup markup,
                                                    int rowIndex, int columnIndex,
                                                    String text, String callbackData) {
        final List<InlineKeyboardButton> row = markup.getKeyboard().get(rowIndex);

        row.add(columnIndex, new InlineKeyboardButton().setText(text)
                .setCallbackData(callbackData));

        return markup;
    }

    public static InlineKeyboardMarkup appendButton(InlineKeyboardMarkup markup,
                                                    String text, String callbackData) {
        markup.getKeyboard().add(Lists.newArrayList(
                new InlineKeyboardButton().setText(text).setCallbackData(callbackData)
        ));

        return markup;
    }
}
