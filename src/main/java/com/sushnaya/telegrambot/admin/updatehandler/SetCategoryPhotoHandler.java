package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.MenuCategory;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.admin.state.AdminBotDialogState;
import com.sushnaya.telegrambot.admin.state.AskPhotoState;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.singleButtonOneTimeKeyboard;

public class SetCategoryPhotoHandler extends EditCategoryHandler {
    public SetCategoryPhotoHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final MenuCategory category = getMenuCategory(update);

        if (category != null) {
            askCategoryPhoto(update, category)
                    .then((u, photoFileId) -> updateMenuCategoryPhoto(u, category, photoFileId))
                    .ifThen(DELETE_OPTIONAL_PROPERTY, u -> deleteMenuCategoryPhoto(u, category))
                    .onCancel(u -> adjustBotState(u, category));

        } else {
            bot.handleUnknownCommand(update);
        }
    }

    private void adjustBotState(Update u, MenuCategory category) {
        bot.setAdminDefaultState(u);
        editCategory(u, category);
    }

    private void updateMenuCategoryPhoto(Update update, MenuCategory category, String photoFileId) {
        updateMenuCategoryPhoto(category, photoFileId);

        bot.say(update, MESSAGES.categoryPhotoUpdateSucceeded(), true);

        adjustBotState(update, category);
    }

    private void updateMenuCategoryPhoto(MenuCategory category, String photoFileId) {
        category.setTelegramPhotoFileId(photoFileId);
        bot.getDataStorage().saveCategory(category);
    }

    private void deleteMenuCategoryPhoto(Update update, MenuCategory category) {
        updateMenuCategoryPhoto(update, category, null);
    }

    private AdminBotDialogState<String> askCategoryPhoto(Update update, MenuCategory category) {
        final ReplyKeyboardMarkup keyboard = category.hasPhoto() ?
                singleButtonOneTimeKeyboard(MESSAGES.deleteCategoryPhoto()) : null;

        return new AskPhotoState(bot).ask(update, MESSAGES.askCategoryPhoto(), keyboard)
                .setExtraCommandParser((u) -> {
                    if (u.hasMessage() && u.getMessage().hasText()) {
                        final String text = u.getMessage().getText();

                        if (text.equalsIgnoreCase(MESSAGES.deleteCategoryPhoto())) {
                            return DELETE_OPTIONAL_PROPERTY;
                        }
                    }

                    return NOP;
                });
    }

    private MenuCategory getMenuCategory(Update update) {
        Integer categoryId = parseCommandUriIntPayload(update);

        return categoryId == null ? null : bot.getDataStorage().getMenuCategory(categoryId);
    }
}
