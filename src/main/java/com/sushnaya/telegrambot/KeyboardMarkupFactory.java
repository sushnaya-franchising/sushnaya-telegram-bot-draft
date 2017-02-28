package com.sushnaya.telegrambot;

import com.google.common.collect.Lists;
import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public interface KeyboardMarkupFactory {

    ReplyKeyboardRemove REPLY_KEYBOARD_REMOVE = new ReplyKeyboardRemove();

    InlineKeyboardMarkup menuMarkup(List<MenuCategory> categories);

    default ReplyKeyboardMarkup oneButtonOneTimeReplyMarkup(String label) {
        final List<KeyboardRow> keyboard = Lists.newArrayList();
        KeyboardRow row = new KeyboardRow();
        row.add(label);
        keyboard.add(row);

        return new ReplyKeyboardMarkup()
                .setKeyboard(keyboard)
                .setResizeKeyboard(true)
                .setOneTimeKeyboad(true);
    }

    InlineKeyboardMarkup menusMarkup(List<Menu> menus);
}
