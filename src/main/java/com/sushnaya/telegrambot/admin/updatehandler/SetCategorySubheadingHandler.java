package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.MenuCategory;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.admin.state.AdminBotDialogState;
import com.sushnaya.telegrambot.admin.state.AskTextState;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.sushnaya.telegrambot.Command.DELETE_OPTIONAL_PROPERTY;
import static com.sushnaya.telegrambot.Command.NOP;
import static com.sushnaya.telegrambot.Command.parseCommandUriIntPayload;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.singleButtonOneTimeKeyboard;

public class SetCategorySubheadingHandler extends EditCategoryHandler {
    public SetCategorySubheadingHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final MenuCategory category = getCategory(update);

        if (category != null) {
            askCategorySubheading(update, category)
                    .then((u, subheading) -> updateCategorySubheading(u, category, subheading))
                    .ifThen(DELETE_OPTIONAL_PROPERTY, u -> deleteCategorySubheading(u, category))
                    .onCancel(u -> adjustBotState(u, category));

        } else {
            bot.handleUnknownCommand(update);
        }
    }

    private void adjustBotState(Update u, MenuCategory category) {
        bot.setAdminDefaultState(u);
        editCategory(u, category);
    }

    private void deleteCategorySubheading(Update u, MenuCategory category) {
        updateCategorySubheading(u, category, null);
    }

    private void updateCategorySubheading(Update u, MenuCategory category, String subheading) {
        updateProductSubheading(category, subheading);

        bot.say(u, MESSAGES.categorySubheadingUpdateSucceeded(), true);

        adjustBotState(u, category);
    }

    private void updateProductSubheading(MenuCategory category, String subheading) {
        category.setSubheading(subheading);
        bot.getDataStorage().saveCategory(category);
    }

    private AdminBotDialogState<String> askCategorySubheading(Update update, MenuCategory category) {
        final ReplyKeyboardMarkup keyboard = category.hasSubheading() ?
                singleButtonOneTimeKeyboard(MESSAGES.deleteCategorySubheading()) : null;

        return new AskTextState(bot).ask(update, MESSAGES.askCategorySubheading(), keyboard)
                .setExtraCommandParser((u) -> {
                    if (u.hasMessage() && u.getMessage().hasText()) {
                        final String text = u.getMessage().getText();

                        if (text.equalsIgnoreCase(MESSAGES.deleteCategorySubheading())) {
                            return DELETE_OPTIONAL_PROPERTY;
                        }
                    }

                    return NOP;
                });
    }

    private MenuCategory getCategory(Update update) {
        Integer categoryId = parseCommandUriIntPayload(update);

        return categoryId == null ? null : bot.getDataStorage().getMenuCategory(categoryId);
    }
}
