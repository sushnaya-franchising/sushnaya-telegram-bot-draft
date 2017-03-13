package com.sushnaya.telegrambot.admin.state;

import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.admin.updatehandler.EditMenuHandler;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.addFirstButton;
import static java.lang.String.format;

public class DeleteCategoryHandler extends EditMenuHandler {
    public DeleteCategoryHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final MenuCategory category = getMenuCategory(update);

        if (category != null) {
            final Menu menu = category.getMenu();

            deleteCategory(category);

            answer(update, menu, category);
        }
    }

    private MenuCategory getMenuCategory(Update update) {
        Integer categoryId = parseCommandUriIntPayload(update);

        return categoryId == null ? null : bot.getDataStorage().getMenuCategory(categoryId);
    }

    private void deleteCategory(MenuCategory category) {
        bot.getDataStorage().deleteCategory(category);
        category.getMenu().removeCategory(category);// todo: danger! implement proper design
    }

    private void answer(Update update, Menu menu, MenuCategory category) {
        final String message = format("%s\n\n%s", getEditMenuMessageText(menu),
                MESSAGES.categoryWasDeleted(category));
        final InlineKeyboardMarkup keyboard = addFirstButton(getEditMenuKeyboard(menu),
                MESSAGES.recoverCategory(category), buildCommandUri(RECOVER_CATEGORY,
                        menu.getId(), category.getId()));

        bot.answer(update, message, keyboard);
    }
}
