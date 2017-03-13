package com.sushnaya.telegrambot.admin.state.dialog;

import com.sushnaya.entity.MenuCategory;
import com.sushnaya.telegrambot.Messages;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.admin.state.AdminBotDialogState;
import com.sushnaya.telegrambot.admin.state.AskPhotoState;
import com.sushnaya.telegrambot.admin.state.AskTextState;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.sushnaya.telegrambot.Command.CANCEL;
import static com.sushnaya.telegrambot.Command.SKIP;

public class CategoryCreationDialog extends AdminBotDialogState<MenuCategory> {
    private static final Messages MESSAGES = Messages.getDefaultMessages();
    private final AskTextState nameStep;
    private final AskTextState subheadingStep;
    private final AskPhotoState photoStep;
    private final AdminBotDialogState[] steps;

    public CategoryCreationDialog(SushnayaBot bot) {
        super(bot);

        // todo: fix all help messages for all cases: first menu creation, first category creation etc
        steps = new AdminBotDialogState[]{
                nameStep = (AskTextState) new AskTextState(bot)
                        .setDefaultMessage(MESSAGES.askCategoryName())
                        .setHelpMessage(MESSAGES.askCategoryNameHelp() + "\n\n" +
                                MESSAGES.cancelMenuCreationHelp(CANCEL)),

                photoStep = (AskPhotoState) new AskPhotoState(bot)
                        .setDefaultMessage(MESSAGES.askCategoryPhoto())
                        .setHelpMessage(MESSAGES.skipCategoryPhotoStepHelp() + "\n\n" +
                                MESSAGES.cancelMenuCreationHelp(CANCEL))
                        .setDefaultKeyboard(bot.getAdminKeyboardFactory().skipCategoryPhotoStepMarkup())
                        .setSkippable(true),

                subheadingStep = (AskTextState) new AskTextState(bot)
                        .setDefaultMessage(MESSAGES.askCategorySubheading())
                        .setHelpMessage((MESSAGES.askCategorySubheadingStepHelp() + "\n\n" +
                                MESSAGES.skipCategorySubheadingStepHelp() + "\n\n" +
                                MESSAGES.cancelMenuCreationHelp(CANCEL)))
                        .setDefaultKeyboard(bot.getAdminKeyboardFactory().skipCategorySubheadingStepMarkup())
                        .setSkippable(true)
        };
    }

    public AskTextState getNameStep() {
        return nameStep;
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

        nameStep.ask(update, message != null ? message : MESSAGES.askCategoryName())
                .then((u, categoryName) -> {
                    category.setName(categoryName);

                    subheadingStep.ask(u, MESSAGES.valueConfirmation(category.getDisplayName()) + " " +
                            MESSAGES.askCategorySubheadingForCategoryCreation());
                });

        subheadingStep.then((u, categorySubheading) -> {
            category.setSubheading(categorySubheading);

            photoStep.ask(u, MESSAGES.askCategoryPhotoForCategoryCreation(category));
        });

        photoStep.then((u, categoryPhotoFilePath) -> {
            category.setTelegramPhotoFileId(categoryPhotoFilePath);

            getThen().accept(u, category);
        });

        return this;
    }

    @Override
    protected void handleUpdate(Update update, BiConsumer<Update, MenuCategory> ok, BiConsumer<Update, String> ko) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
