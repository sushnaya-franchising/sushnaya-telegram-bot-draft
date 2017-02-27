package com.sushnaya.telegrambot.dialog;

import com.sushnaya.entity.MenuCategory;
import com.sushnaya.telegrambot.AdminKeyboardMarkupFactory;
import com.sushnaya.telegrambot.Messages;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.state.admin.AdminBotDialogState;
import com.sushnaya.telegrambot.state.admin.AskPhotoState;
import com.sushnaya.telegrambot.state.admin.AskTextState;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.sushnaya.telegrambot.Command.CANCEL;
import static com.sushnaya.telegrambot.Command.SKIP;

public class CategoryCreationDialog extends AdminBotDialogState<MenuCategory> {
    private static final Messages MESSAGES = Messages.getDefaultMessages();
    private final AskTextState categoryNameStep;
    private final AskTextState categorySubheadingStep;
    private final AskPhotoState categoryPhotoStep;
    private final AdminBotDialogState[] steps;

    public CategoryCreationDialog(SushnayaBot bot, final AdminKeyboardMarkupFactory keyboardMarkupFactory) {
        super(bot);

        steps = new AdminBotDialogState[]{
                categoryNameStep = (AskTextState) new AskTextState(bot)
                        .setDefaultMessage(MESSAGES.askCategoryName())
                        .setHelpMessage(MESSAGES.askCategoryNameHelp() + "\n\n" +
                                MESSAGES.cancelMenuCreationHelp(CANCEL)),

                categoryPhotoStep = (AskPhotoState) new AskPhotoState(bot)
                        .setDefaultMessage(MESSAGES.askCategoryPhoto())
                        .setHelpMessage(MESSAGES.skipCategoryPhotoStepHelp(SKIP) + "\n\n" +
                                MESSAGES.cancelMenuCreationHelp(CANCEL))
                        .setDefaultKeyboard(keyboardMarkupFactory.skipCategoryPhotoStepMarkup())
                        .setSkippable(true),

                categorySubheadingStep = (AskTextState) new AskTextState(bot)
                        .setDefaultMessage(MESSAGES.askCategorySubheading())
                        .setHelpMessage((MESSAGES.askCategorySubheadingStepHelp() + "\n\n" +
                                MESSAGES.skipCategorySubheadingStepHelp(SKIP) + "\n\n" +
                                MESSAGES.cancelMenuCreationHelp(CANCEL)))
                        .setDefaultKeyboard(keyboardMarkupFactory.skipCategorySubheadingStepMarkup())
                        .setSkippable(true)
        };
    }

    public AskTextState getCategoryNameStep() {
        return categoryNameStep;
    }

    @Override
    public AdminBotDialogState<MenuCategory> onCancel(Consumer<Update> onCancel) {
        for (AdminBotDialogState step : steps) {
            step.onCancel(onCancel);
        }

        return super.onCancel(onCancel);
    }

    @Override
    public AdminBotDialogState<MenuCategory> ask(Update update, String message, ReplyKeyboard keyboard) {
        MenuCategory category = new MenuCategory();

        categoryNameStep.ask(update, message != null ? message : MESSAGES.askCategoryName())
                .then((u, categoryName) -> {
                    category.setName(categoryName);

                    categorySubheadingStep.ask(u, MESSAGES.valueConfirmation(category.getDisplayName()) + " " +
                            MESSAGES.askCategorySubheadingForCategoryCreation());
                });

        categorySubheadingStep.then((u, categorySubheading) -> {
            category.setSubheading(categorySubheading);

            categoryPhotoStep.ask(u, MESSAGES.askCategoryPhotoForCategoryCreation(category));
        });

        categoryPhotoStep.then((u, categoryPhotoFilePath) -> {
            category.setTelegramFilePath(categoryPhotoFilePath);

            getThen().accept(u, category);
        });

        return this;
    }

    @Override
    protected void handleUpdate(Update update, BiConsumer<Update, MenuCategory> ok, BiConsumer<Update, String> ko) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
