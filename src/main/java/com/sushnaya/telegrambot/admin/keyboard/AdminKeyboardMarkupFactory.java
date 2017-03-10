package com.sushnaya.telegrambot.admin.keyboard;

import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import com.sushnaya.entity.Product;
import com.sushnaya.telegrambot.KeyboardMarkupFactory;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;

public interface AdminKeyboardMarkupFactory extends KeyboardMarkupFactory {

    InlineKeyboardMarkup dashboardMarkup();

    ReplyKeyboard editMenuLocalityMarkup();

    ReplyKeyboard skipCategorySubheadingStepMarkup();

    ReplyKeyboard skipCategoryPhotoStepMarkup();

    ReplyKeyboard skipProductPhotoStepMarkup();

    ReplyKeyboard skipProductSubheadingStepMarkup();

    ReplyKeyboard skipProductDescriptionStepMarkup();

    InlineKeyboardMarkup localityAlreadyBoundToMenuMarkup();

    default InlineKeyboardMarkup menuCreationFurtherCommands(Menu menu) {
        return menuCreationFurtherCommands(menu, null);
    }

    default InlineKeyboardMarkup categoryCreationFurtherCommands(Menu menu, MenuCategory categoryToCreateProductIn) {
        return menuCreationFurtherCommands(menu, categoryToCreateProductIn);
    }

    default InlineKeyboardMarkup productCreationFurtherCommands(Menu menu, MenuCategory category){
        return menuCreationFurtherCommands(menu, category);
    }

    InlineKeyboardMarkup menuCreationFurtherCommands(Menu menu, MenuCategory categoryToCreateProductIn);

    ReplyKeyboard productCreationCompletion(boolean suggestToAddSubheading, boolean suggestToAddDescription);

    InlineKeyboardMarkup editMenu(Menu menu);

    InlineKeyboardMarkup editCategory(MenuCategory category);

    InlineKeyboardMarkup editProduct(Product product);
}
