package com.sushnaya.telegrambot.user.keyboard;

import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import com.sushnaya.telegrambot.KeyboardMarkupFactory;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.selectCategoryKeyboard;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.selectMenuKeyboard;

public class UserKeyboardMarkupFactory implements KeyboardMarkupFactory {
    @Override
    public InlineKeyboardMarkup menuCategoriesKeyboard(List<MenuCategory> categories) {
        return selectCategoryKeyboard(categories);
    }

    @Override
    public InlineKeyboardMarkup menusKeyboard(List<Menu> menus) {
        return selectMenuKeyboard(menus);
    }
}
