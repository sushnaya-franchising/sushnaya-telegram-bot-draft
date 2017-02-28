package com.sushnaya.telegrambot;

import com.google.common.collect.Lists;
import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.sushnaya.telegrambot.Command.CATEGORY;
import static com.sushnaya.telegrambot.Command.MENU;

public class DefaultKeyboardMarkupFactory implements KeyboardMarkupFactory {

    public InlineKeyboardMarkup menuMarkup(List<MenuCategory> categories) {
        final List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        List<InlineKeyboardButton> row = null;
        for (int i = 0; i < categories.size(); i++) {
            if (i % 2 == 0) {
                if (row != null) keyboard.add(row);
                row = Lists.newArrayList();
            }

            MenuCategory c = categories.get(i);
            row.add(new InlineKeyboardButton().setText(c.getDisplayName())
                    .setCallbackData(CATEGORY.getUriForId(c.getId())));
        }
        keyboard.add(row);

        // todo: add search button

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }

    @Override
    public InlineKeyboardMarkup menusMarkup(List<Menu> menus) {
        final List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        List<InlineKeyboardButton> row = null;
        for (int i = 0; i < menus.size(); i++) {
            if (i % 2 == 0) {
                if (row != null) keyboard.add(row);
                row = Lists.newArrayList();
            }

            Menu menu = menus.get(i);
            row.add(new InlineKeyboardButton().setText(menu.getLocality().getName())
                    .setCallbackData(MENU.getUriForId(menu.getId())));
        }
        keyboard.add(row);

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }
}
