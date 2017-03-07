package com.sushnaya.telegrambot.admin.state.dialog;

import com.sushnaya.entity.Menu;
import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.admin.state.AdminBotDialogState;
import com.sushnaya.telegrambot.admin.state.AskCommandState;
import com.sushnaya.telegrambot.admin.state.AskLocalityState;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;

public class MenuCreationDialog extends AdminBotDialogState<Menu> {
    public static final Function<Update, Command> EDIT_LOCALITY_COMMAND_PARSER = u -> {
        if (u.hasMessage() && u.getMessage().hasText()) {
            final String text = u.getMessage().getText();
            if (text.equalsIgnoreCase(MESSAGES.editLocality())) {
                return EDIT_LOCALITY;
            }
        }
        return NOP;
    };

    private final AdminBotDialogState[] steps;
    private final AskLocalityState localityStep;
    private final AskCommandState localityConfirmationStep;
    private final CategoryCreationDialog categoryCreationDialog;
    private final ProductCreationDialog productCreationDialog;

    public MenuCreationDialog(final SushnayaBot bot) {
        super(bot);

        steps = new AdminBotDialogState[]{
                localityStep = (AskLocalityState) new AskLocalityState(bot)
                        .setDefaultMessage(MESSAGES.askLocality())
                        .setHelpMessage(MESSAGES.askLocalityHelp() + "\n\n" +
                                MESSAGES.cancelMenuCreationHelp(CANCEL)),

                localityConfirmationStep = (AskCommandState) new AskCommandState(bot)
                        .setHelpMessage(MESSAGES.cancelMenuCreationHelp(CANCEL))
                        .setDefaultKeyboard(bot.getAdminKeyboardFactory().localityAlreadyBoundToMenuMarkup())
                        .ifThen(EDIT_LOCALITY, localityStep::ask)
                        .ifThen(ADMIN_DASHBOARD, u -> {
                            bot.setAdminDefaultState(u);
                            bot.handleCommand(u, ADMIN_DASHBOARD);
                        }),

                categoryCreationDialog = new CategoryCreationDialog(bot),

                productCreationDialog = new ProductCreationDialog(bot)
        };

        categoryCreationDialog.getNameStep()
                .setDefaultKeyboard(bot.getAdminKeyboardFactory().editMenuLocalityMarkup())
                .ifThen(EDIT_LOCALITY, localityStep::ask)
                .setExtraCommandParser(EDIT_LOCALITY_COMMAND_PARSER);
    }

    @Override
    protected void handleUpdate(Update update, BiConsumer<Update, Menu> ok,
                                BiConsumer<Update, String> ko) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AdminBotDialogState<Menu> onCancel(Consumer<Update> onCancel) {
        for (AdminBotDialogState step : steps) {
            step.onCancel(onCancel);
        }

        return super.onCancel(onCancel);
    }

    @Override
    public AdminBotDialogState<Menu> ask(Update update, String message, ReplyKeyboard keyboard) {
        final Menu menu = new Menu();

        localityStep.ask(update, message != null ? message : MESSAGES.askLocalityForMenuCreation())
                .then((u, locality) -> {
                    menu.setLocality(locality);
                    // reveal resolved location
                    bot.say(u, locality.getCoordinate());

                    if (bot.isLocalityAlreadyBoundToMenu(locality)) {
                        localityConfirmationStep.ask(u,
                                MESSAGES.askLocalityConfirmationForMenuCreation(locality));

                    } else {
                        categoryCreationDialog.ask(u, getCategoryCreationAskMessage(menu));
                    }
                });

        localityConfirmationStep.ifThen(CONTINUE, (u) ->
                categoryCreationDialog.ask(u, getCategoryCreationAskMessage(menu)));

        categoryCreationDialog.then((u, category) -> {
            menu.addCategory(category);

            productCreationDialog.ask(u, MESSAGES.askProductNameForMenuCreation(category));
        });

        productCreationDialog.then((u, product) -> {
            menu.getFirstCategory().addProduct(product);

            getThen().accept(u, menu);
        });

        return this;
    }

    private String getCategoryCreationAskMessage(Menu menu) {
        return MESSAGES.valueConfirmation(menu.getLocality().getFullName()) + " " +
                (bot.hasMenu() ? MESSAGES.askCategoryNameForMenuCreation() :
                        MESSAGES.askCategoryNameForFirstMenuCreation());
    }
}
