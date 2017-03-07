package com.sushnaya.telegrambot.admin.keyboard;

import com.google.common.collect.Lists;
import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import com.sushnaya.telegrambot.util.KeyboardMarkupUtil;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.singleButtonOneTimeKeyboard;

class StartupAdminKeyboardMarkupFactory implements AdminKeyboardMarkupFactory {

    public InlineKeyboardMarkup selectMenuKeyboard(List<Menu> menus) {
        return KeyboardMarkupUtil.selectMenuKeyboard(menus);
    }

    public InlineKeyboardMarkup selectCategoryKeyboard(Menu menu) {
        return KeyboardMarkupUtil.selectCategoryKeyboard(menu.getMenuCategories());
    }

    public InlineKeyboardMarkup dashboardMarkup() {
        List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.createMenu())
                        .setCallbackData(CREATE_MENU.getUri())
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.statistics())
                        .setCallbackData(STATISTICS.getUri()),
                new InlineKeyboardButton().setText(MESSAGES.notify_())
                        .setCallbackData(NOTIFY.getUri())
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.promotions())
                        .setCallbackData(PROMOTIONS.getUri()),
                new InlineKeyboardButton().setText(MESSAGES.settings())
                        .setCallbackData(SETTINGS.getUri())
        ));

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }

    public ReplyKeyboard editMenuLocalityMarkup() {
        return singleButtonOneTimeKeyboard(MESSAGES.editLocality());
    }

    public ReplyKeyboard skipCategoryPhotoStepMarkup() {
        return singleButtonOneTimeKeyboard(MESSAGES.skip());
    }

    public ReplyKeyboard skipCategorySubheadingStepMarkup() {
        return singleButtonOneTimeKeyboard(MESSAGES.skip());
    }

    public ReplyKeyboard skipProductPhotoStepMarkup() {
        return singleButtonOneTimeKeyboard(MESSAGES.skip());
    }

    @Override
    public ReplyKeyboard skipProductSubheadingStepMarkup() {
        return singleButtonOneTimeKeyboard(MESSAGES.skip());
    }

    @Override
    public ReplyKeyboard skipProductDescriptionStepMarkup() {
        return singleButtonOneTimeKeyboard(MESSAGES.skip());
    }

    public InlineKeyboardMarkup localityAlreadyBoundToMenuMarkup() {
        List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.editLocality())
                        .setCallbackData(EDIT_LOCALITY.getUri())
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.continueMessage())
                        .setCallbackData(CONTINUE.getUri())
        ));
        appendBackToDashboardButton(keyboard);

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }

    public InlineKeyboardMarkup menuCreationFurtherCommands(
            Menu menu, MenuCategory categoryToCreateProductIn) {
        final List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        final int categoriesCount = menu.getMenuCategories().size();
        final MenuCategory onlyCategory = categoriesCount == 1 ?
                menu.getFirstCategory() : categoryToCreateProductIn;

        if (onlyCategory != null) appendCreateProductInCategoryButton(keyboard, onlyCategory);
        if (categoriesCount > 1) appendCreateProductInMenuButton(keyboard, menu);
        appendCreateCategoryButton(keyboard, menu);
        appendBackToDashboardButton(keyboard);

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }

    protected void appendBackToDashboardButton(List<List<InlineKeyboardButton>> keyboard) {
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.backToDashboard())
                        .setCallbackData(MENU.getUri())
        ));
    }

    protected void appendCreateCategoryButton(
            List<List<InlineKeyboardButton>> keyboard, Menu menu) {
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.createCategory())
                        .setCallbackData(buildCommandUri(CREATE_CATEGORY, menu.getId()))
        ));
    }

    protected void appendCreateProductInMenuButton(
            List<List<InlineKeyboardButton>> keyboard, Menu menu) {
        keyboard.add(Lists.newArrayList(new InlineKeyboardButton()
                .setText(MESSAGES.createProductInMenu())
                .setCallbackData(buildCommandUri(CREATE_PRODUCT, menu.getId()))
        ));
    }

    protected void appendCreateProductInCategoryButton(
            List<List<InlineKeyboardButton>> keyboard, MenuCategory onlyCategory) {
        keyboard.add(Lists.newArrayList(new InlineKeyboardButton()
                .setText(MESSAGES.createProductInCategory(onlyCategory.getDisplayName()))
                .setCallbackData(buildCommandUri(CREATE_PRODUCT_IN_CATEGORY, onlyCategory.getId()))));
    }

    @Override
    public ReplyKeyboard productCreationCompletion(
            boolean suggestToAddSubheading, boolean suggestToAddDescription) {
        final List<KeyboardRow> keyboard = Lists.newArrayList();
        KeyboardRow row = new KeyboardRow();

        if (suggestToAddSubheading) {
            row.add(MESSAGES.setProductSubheading());
            keyboard.add(row);
            row = new KeyboardRow();
        }

        if (suggestToAddDescription) {
            row.add(MESSAGES.setProductDescription());
            keyboard.add(row);
            row = new KeyboardRow();
        }

        row.add(MESSAGES.skipProductPublication());
        row.add(MESSAGES.publishProduct());
        keyboard.add(row);

        return new ReplyKeyboardMarkup().setKeyboard(keyboard).setResizeKeyboard(true)
                .setOneTimeKeyboad(true);
    }
}