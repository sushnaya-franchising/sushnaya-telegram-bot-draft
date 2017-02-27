package com.sushnaya.telegrambot;

import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

public interface AdminKeyboardMarkupFactory extends KeyboardMarkupFactory {
    InlineKeyboardMarkup dashboardMarkup();

    ReplyKeyboard editMenuLocalityMarkup();

    ReplyKeyboard skipCategorySubheadingStepMarkup();

    ReplyKeyboard skipCategoryPhotoStepMarkup();

    ReplyKeyboard skipProductPhotoStepMarkup();

    ReplyKeyboard skipDescriptionStepMarkup();

    InlineKeyboardMarkup localityAlreadyBoundToMenuMarkup();

    InlineKeyboardMarkup editMenuMarkup(Menu menu);

    InlineKeyboardMarkup proposalToAddOneMoreProductMarkup(MenuCategory category);

    InlineKeyboardMarkup editMenus(List<Menu> menus);

    ReplyKeyboard productCreationCompletion(boolean suggestToAddSubheading, boolean suggestToAddDescription);

    ReplyKeyboard skipProductSubheadingStepMarkup();

    ReplyKeyboard skipProductDescriptionStepMarkup();
}
