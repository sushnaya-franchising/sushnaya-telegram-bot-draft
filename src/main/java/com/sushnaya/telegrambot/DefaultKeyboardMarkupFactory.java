package com.sushnaya.telegrambot;

import com.google.common.collect.Lists;
import com.sushnaya.entity.MenuCategory;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.sushnaya.telegrambot.Command.MENU;
import static com.sushnaya.telegrambot.Command.SEARCH;

public class DefaultKeyboardMarkupFactory implements KeyboardMarkupFactory {

    public InlineKeyboardMarkup homeMarkup() {
        final InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        List<InlineKeyboardButton> row = Lists.newArrayList(
                new InlineKeyboardButton().setText(MENU.getText())
                        .setCallbackData(MENU.getUri()),
                new InlineKeyboardButton().setText(SEARCH.getText())
                        .setSwitchInlineQueryCurrentChat(StringUtils.EMPTY)
        );
        keyboard.add(row);
        markup.setKeyboard(keyboard);

        return markup;
    }

    public InlineKeyboardMarkup categoriesMarkup(List<MenuCategory> categories) {
        return null;
    }
}
