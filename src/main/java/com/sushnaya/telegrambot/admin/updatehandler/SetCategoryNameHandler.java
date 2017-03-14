package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.MenuCategory;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.admin.state.AdminBotDialogState;
import com.sushnaya.telegrambot.admin.state.AskTextState;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.Command.parseCommandUriIntPayload;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;

public class SetCategoryNameHandler extends EditCategoryHandler {
    public SetCategoryNameHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final MenuCategory category = getCategory(update);

        if (category != null) {
            askCategoryName(update).then((u, name) -> updateCategoryName(u, category, name))
                    .onCancel(u -> adjustBotState(u, category));

        } else {
            bot.handleUnknownCommand(update);
        }
    }

    private void updateCategoryName(Update u, MenuCategory category, String name) {
        updateCategoryName(category, name);

        bot.say(u, MESSAGES.categoryNameUpdateSucceeded(), true);

        adjustBotState(u, category);
    }

    private void adjustBotState(Update u, MenuCategory category) {
        bot.setAdminDefaultState(u);
        editCategory(u, category);
    }

    private void updateCategoryName(MenuCategory category, String name) {
        category.setName(name);
        bot.getDataStorage().saveCategory(category);
    }

    private AdminBotDialogState<String> askCategoryName(Update update) {
        return new AskTextState(bot).ask(update, MESSAGES.askCategoryName(), true);
    }

    private MenuCategory getCategory(Update update) {
        Integer categoryId = parseCommandUriIntPayload(update);

        return categoryId == null ? null : bot.getDataStorage().getMenuCategory(categoryId);
    }
}
