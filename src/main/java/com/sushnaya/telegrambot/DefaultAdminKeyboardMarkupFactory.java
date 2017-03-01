package com.sushnaya.telegrambot;

import com.sushnaya.entity.Menu;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.appendButton;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.insertButton;

public class DefaultAdminKeyboardMarkupFactory extends StartupAdminKeyboardMarkupFactory {
    private static final DefaultKeyboardMarkupFactory DEFAULT_MARKUP_FACTORY =
            new DefaultKeyboardMarkupFactory();

    @Override
    public InlineKeyboardMarkup selectMenu(List<Menu> menus) {
        return appendDashboardButton(DEFAULT_MARKUP_FACTORY.selectMenu(menus));
    }

    @Override
    public InlineKeyboardMarkup menu(Menu menu) {
        return appendDashboardButton(DEFAULT_MARKUP_FACTORY.menu(menu));
    }

    public InlineKeyboardMarkup dashboardMarkup() {
        return appendCloseDashboardButton(
                insertEditMenuButton(super.dashboardMarkup()));
    }

    private InlineKeyboardMarkup insertEditMenuButton(InlineKeyboardMarkup dashboardMarkup) {
        return insertButton(dashboardMarkup, 0, 0,
                EDIT_MENU.getText(), EDIT_MENU.getUri());
    }

    private InlineKeyboardMarkup appendCloseDashboardButton(InlineKeyboardMarkup markup) {
        return appendButton(markup, CLOSE_DASHBOARD.getText(), CLOSE_DASHBOARD.getUri());
    }

    private InlineKeyboardMarkup appendDashboardButton(InlineKeyboardMarkup markup) {
        return appendButton(markup, ADMIN_DASHBOARD.getText(), ADMIN_DASHBOARD.getUri());
    }
}
