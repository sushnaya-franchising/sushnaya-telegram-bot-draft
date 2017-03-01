package com.sushnaya.telegrambot;

import com.google.common.collect.Lists;
import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public interface KeyboardMarkupFactory {

    ReplyKeyboardRemove REPLY_KEYBOARD_REMOVE = new ReplyKeyboardRemove();

    default InlineKeyboardMarkup selectMenu(List<Menu> menus) {
        return menus(menus, Command.MENU);
    }

    default InlineKeyboardMarkup menu(Menu menu) {
        return categories(menu.getMenuCategoriesWithPublishedProducts(), Command.CATEGORY);
    }

    default ReplyKeyboardMarkup oneButtonOneTimeReplyMarkup(String label) {
        final List<KeyboardRow> keyboard = Lists.newArrayList();
        KeyboardRow row = new KeyboardRow();
        row.add(label);
        keyboard.add(row);

        return new ReplyKeyboardMarkup().setKeyboard(keyboard).setResizeKeyboard(true)
                .setOneTimeKeyboad(true);
    }

    default InlineKeyboardMarkup menus(List<Menu> menus, Command command) {
        final List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        List<InlineKeyboardButton> row = null;
        for (int i = 0; i < menus.size(); i++) {
            if (i % 2 == 0) {
                if (row != null) keyboard.add(row);
                row = Lists.newArrayList();
            }

            Menu menu = menus.get(i);
            row.add(new InlineKeyboardButton().setText(menu.getLocality().getName())
                    .setCallbackData(command.getUriForId(menu.getId())));
        }
        keyboard.add(row);

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }

    default InlineKeyboardMarkup categories(List<MenuCategory> categories, Command command) {
        final List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        List<InlineKeyboardButton> row = null;
        for (int i = 0; i < categories.size(); i++) {
            if (i % 2 == 0) {
                if (row != null) keyboard.add(row);
                row = Lists.newArrayList();
            }

            MenuCategory c = categories.get(i);
            row.add(new InlineKeyboardButton().setText(c.getDisplayName())
                    .setCallbackData(command.getUriForId(c.getId())));
        }
        keyboard.add(row);

        // todo: add search button when search will be integrated

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }
}
