package com.sushnaya.telegrambot;

import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.sushnaya.telegrambot.Command.EDIT_MENU;

public class NoPublishedProductsAdminKeyboardMarkupFactory extends StartupAdminKeyboardMarkupFactory {

    @Override
    public InlineKeyboardMarkup dashboardMarkup() {
        final InlineKeyboardMarkup dashboardMarkup = super.dashboardMarkup();
        final List<InlineKeyboardButton> firstRowButtons = dashboardMarkup.getKeyboard().get(0);

        firstRowButtons.add(0, new InlineKeyboardButton().setText(EDIT_MENU.getText())
                .setCallbackData(EDIT_MENU.getUri()));

        return dashboardMarkup;
    }
}
