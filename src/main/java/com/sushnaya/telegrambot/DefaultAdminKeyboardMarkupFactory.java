package com.sushnaya.telegrambot;

import com.google.common.collect.Lists;
import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.sushnaya.telegrambot.Command.ADMIN_DASHBOARD;
import static com.sushnaya.telegrambot.Command.CLOSE_DASHBOARD;
import static com.sushnaya.telegrambot.Command.EDIT_MENU;

public class DefaultAdminKeyboardMarkupFactory extends StartupAdminKeyboardMarkupFactory {
    private static final KeyboardMarkupFactory DEFAULT_KEYBOARD_MARKUP_FACTORY =
            new DefaultKeyboardMarkupFactory();

    public InlineKeyboardMarkup menuMarkup(List<MenuCategory> categories) {
        return appendButton(DEFAULT_KEYBOARD_MARKUP_FACTORY.menuMarkup(categories),
                ADMIN_DASHBOARD.getText(), ADMIN_DASHBOARD.getUri());
    }

    @Override
    public InlineKeyboardMarkup menusMarkup(List<Menu> menus) {
        return appendButton(DEFAULT_KEYBOARD_MARKUP_FACTORY.menusMarkup(menus),
                ADMIN_DASHBOARD.getText(), ADMIN_DASHBOARD.getUri());
    }

    public InlineKeyboardMarkup dashboardMarkup() {
        final InlineKeyboardMarkup dashboardMarkup = super.dashboardMarkup();
        final List<InlineKeyboardButton> firstRowButtons = dashboardMarkup.getKeyboard().get(0);

        firstRowButtons.add(0, new InlineKeyboardButton().setText(EDIT_MENU.getText())
                .setCallbackData(EDIT_MENU.getUri()));

        return appendCloseDashboardButton(dashboardMarkup);
    }

    private InlineKeyboardMarkup appendCloseDashboardButton(InlineKeyboardMarkup markup) {
        return appendButton(markup, CLOSE_DASHBOARD.getText(), CLOSE_DASHBOARD.getUri());
    }

    private InlineKeyboardMarkup appendButton(InlineKeyboardMarkup markup, String label, String commandUri) {
        markup.getKeyboard().add(Lists.newArrayList(
                new InlineKeyboardButton().setText(label).setCallbackData(commandUri)
        ));

        return markup;
    }
}
