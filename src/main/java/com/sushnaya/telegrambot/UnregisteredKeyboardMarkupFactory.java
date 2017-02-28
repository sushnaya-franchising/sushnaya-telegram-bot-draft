package com.sushnaya.telegrambot;

import com.google.common.collect.Lists;
import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

import static com.sushnaya.telegrambot.Command.SEND_CONTACT;

public class UnregisteredKeyboardMarkupFactory implements KeyboardMarkupFactory {

    public ReplyKeyboard contactRequestMarkup() {
        List<KeyboardRow> keyboard = Lists.newArrayList();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton signupButton = new KeyboardButton(SEND_CONTACT.getText());
        signupButton.setRequestContact(true);
        row.add(signupButton);
        keyboard.add(row);

        return new ReplyKeyboardMarkup()
                .setKeyboard(keyboard)
                .setResizeKeyboard(true)
                .setOneTimeKeyboad(true);
    }

    public InlineKeyboardMarkup menuMarkup(List<MenuCategory> categories) {
        return null;
    }

    @Override
    public InlineKeyboardMarkup menusMarkup(List<Menu> menus) {
        return null;
    }
}
