package com.sushnaya.telegrambot;

import com.google.common.collect.Lists;
import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

import static com.sushnaya.telegrambot.Command.*;

public class StartupAdminKeyboardMarkupFactory implements AdminKeyboardMarkupFactory {

    public InlineKeyboardMarkup homeMarkup() {
        return null;
    }

    public InlineKeyboardMarkup categoriesMarkup(List<MenuCategory> categories) {
        final List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        List<InlineKeyboardButton> row = null;
        for (int i = 0; i < categories.size(); i++) {
            if (i % 2 == 0) {
                if (row != null) keyboard.add(row);
                row = Lists.newArrayList();
            }

            MenuCategory c = categories.get(i);
            row.add(new InlineKeyboardButton().setText(c.getDisplayName())
                    .setCallbackData(EDIT_CATEGORY.getUriForId(c.getId())));
        }

        keyboard.add(row);

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }

    public InlineKeyboardMarkup dashboardMarkup() {
        List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(CREATE_MENU.getText())
                        .setCallbackData(CREATE_MENU.getUri())
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(STATISTICS.getText())
                        .setCallbackData(STATISTICS.getUri()),
                new InlineKeyboardButton().setText(NOTIFY.getText())
                        .setCallbackData(NOTIFY.getUri())
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(PROMOTIONS.getText())
                        .setCallbackData(PROMOTIONS.getUri()),
                new InlineKeyboardButton().setText(SETTINGS.getText())
                        .setCallbackData(SETTINGS.getUri())
        ));

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }

    public ReplyKeyboard editMenuLocalityMarkup() {
        return oneButtonOneTimeReplyMarkup(EDIT_LOCALITY.getText());
    }

    public ReplyKeyboard skipCategoryPhotoStepMarkup() {
        return oneButtonOneTimeReplyMarkup(SKIP.getText());
    }

    @Override
    public ReplyKeyboard skipProductPhotoStepMarkup() {
        return oneButtonOneTimeReplyMarkup(SKIP.getText());
    }

    public ReplyKeyboard skipDescriptionStepMarkup() {
        return oneButtonOneTimeReplyMarkup(SKIP.getText());
    }

    public ReplyKeyboard skipCategorySubheadingStepMarkup() {
        return oneButtonOneTimeReplyMarkup(SKIP.getText());
    }

    public InlineKeyboardMarkup localityAlreadyBoundToMenuMarkup() {
        List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(EDIT_LOCALITY.getText())
                        .setCallbackData(EDIT_LOCALITY.getUri())
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(CONTINUE.getText())
                        .setCallbackData(CONTINUE.getUri())
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(BACK_TO_DASHBOARD.getText())
                        .setCallbackData(BACK_TO_DASHBOARD.getUri())
        ));

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }

    public InlineKeyboardMarkup editMenuMarkup(Menu menu) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public InlineKeyboardMarkup proposalToAddOneMoreProductMarkup(MenuCategory category) {
        final List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton()
                        .setText(CREATE_PRODUCT_IN_CATEGORY.getText() +
                                " \"" + category.getDisplayName() + "\"")
                        .setCallbackData(CREATE_PRODUCT_IN_CATEGORY.getUriForId(category.getId()))));

        if (category.getMenu().getMenuCategories().size() > 1) {
            keyboard.add(Lists.newArrayList(
                    new InlineKeyboardButton().setText(CREATE_PRODUCT_IN_MENU.getText())
                            .setCallbackData(CREATE_PRODUCT_IN_MENU.getUriForId(
                                    category.getMenu().getId()))
            ));
        }

        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(BACK_TO_DASHBOARD.getText())
                        .setCallbackData(BACK_TO_DASHBOARD.getUri())
        ));

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }


    public InlineKeyboardMarkup editMenus(List<Menu> menus) {
        final List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();

        if (!menus.isEmpty()) {
            List<InlineKeyboardButton> row = null;
            for (int i = 0; i < menus.size(); i++) {
                if (i % 2 == 0) {
                    if (row != null) keyboard.add(row);
                    row = Lists.newArrayList();
                }
                Menu menu = menus.get(i);
                row.add(new InlineKeyboardButton().setText(menu.getLocality().getDisplayName())
                        .setCallbackData(EDIT_MENU.getUriForId(menu.getId())));
            }
            keyboard.add(row);
        }

        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(CREATE_MENU.getText())
                        .setCallbackData(CREATE_MENU.getUri())
        ));

        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(BACK_TO_DASHBOARD.getText())
                        .setCallbackData(BACK_TO_DASHBOARD.getUri())
        ));

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }

    public ReplyKeyboard contactRequestMarkup() {
        return null;
    }

    @Override
    public ReplyKeyboard productCreationCompletion(
            boolean suggestToAddSubheading, boolean suggestToAddDescription) {
        final List<KeyboardRow> keyboard = Lists.newArrayList();
        KeyboardRow row = new KeyboardRow();

        if (suggestToAddSubheading) {
            row.add(SET_PRODUCT_SUBHEADING.getText());
            keyboard.add(row);
            row = new KeyboardRow();
        }

        if (suggestToAddDescription) {
            row.add(SET_PRODUCT_DESCRIPTION.getText());
            keyboard.add(row);
            row = new KeyboardRow();
        }

        row.add(SKIP_PRODUCT_PUBLICATION.getText());
        row.add(PUBLISH_PRODUCT.getText());
        keyboard.add(row);

        return new ReplyKeyboardMarkup()
                .setKeyboard(keyboard)
                .setResizeKeyboard(true)
                .setOneTimeKeyboad(true);
    }

    @Override
    public ReplyKeyboard skipProductSubheadingStepMarkup() {
        return oneButtonOneTimeReplyMarkup(SKIP.getText());
    }

    @Override
    public ReplyKeyboard skipProductDescriptionStepMarkup() {
        return oneButtonOneTimeReplyMarkup(SKIP.getText());
    }
}
