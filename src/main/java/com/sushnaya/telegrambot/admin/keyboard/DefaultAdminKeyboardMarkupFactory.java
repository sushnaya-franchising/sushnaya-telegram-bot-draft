package com.sushnaya.telegrambot.admin.keyboard;

import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import com.sushnaya.telegrambot.util.KeyboardMarkupUtil;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.appendButton;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.insertButton;

class DefaultAdminKeyboardMarkupFactory extends StartupAdminKeyboardMarkupFactory {

    public InlineKeyboardMarkup menusKeyboard(List<Menu> menus) {
        return appendDashboardButton(KeyboardMarkupUtil.selectMenuKeyboard(menus));
    }

    public InlineKeyboardMarkup menuCategoriesKeyboard(List<MenuCategory> categories) {
        return appendDashboardButton(KeyboardMarkupUtil.selectCategoryKeyboard(categories));
    }

    public InlineKeyboardMarkup dashboardMarkup() {
        return appendCloseDashboardButton(
                insertEditMenuButton(super.dashboardMarkup()));
    }

    private InlineKeyboardMarkup insertEditMenuButton(InlineKeyboardMarkup dashboardMarkup) {
        return insertButton(dashboardMarkup, 0, 0,
                MESSAGES.editMenu(), EDIT_MENU.getUri());
    }

    private InlineKeyboardMarkup appendCloseDashboardButton(InlineKeyboardMarkup markup) {
        return appendButton(markup, MESSAGES.backToMenu(), MENU.getUri());
    }

    private InlineKeyboardMarkup appendDashboardButton(InlineKeyboardMarkup markup) {
        return appendButton(markup, MESSAGES.dashboard(), ADMIN_DASHBOARD.getUri());
    }
}
