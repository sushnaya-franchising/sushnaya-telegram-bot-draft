package com.sushnaya.telegrambot;

import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import com.sushnaya.entity.Product;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

public interface KeyboardMarkupFactory {
    InlineKeyboardMarkup menuCategoriesKeyboard(List<MenuCategory> categories);

    InlineKeyboardMarkup menusKeyboard(List<Menu> menus);

    InlineKeyboardMarkup nextProductInCategoryKeyboard(Product product, int cursor, int productsCount);
}
