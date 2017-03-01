package com.sushnaya.telegrambot;

import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
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

    InlineKeyboardMarkup editMenuMarkup(Menu menu);

    default InlineKeyboardMarkup menuCreationFurtherCommands(Menu menu) {
        return menuCreationFurtherCommands(menu, null);
    }

    InlineKeyboardMarkup menuCreationFurtherCommands(Menu menu, MenuCategory categoryToCreateProductIn);

    default InlineKeyboardMarkup categoryCreationFurtherCommands(Menu menu, MenuCategory categoryToCreateProductIn) {
        return menuCreationFurtherCommands(menu, categoryToCreateProductIn);
    }

    default InlineKeyboardMarkup productCreationFurtherCommands(Menu menu, MenuCategory category){
        return menuCreationFurtherCommands(menu, category);
    }

    ReplyKeyboard productCreationCompletion(boolean suggestToAddSubheading, boolean suggestToAddDescription);

}
