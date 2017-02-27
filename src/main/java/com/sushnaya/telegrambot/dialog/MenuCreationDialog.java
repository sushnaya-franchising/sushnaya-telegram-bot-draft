package com.sushnaya.telegrambot.dialog;

import com.sushnaya.entity.Menu;
import com.sushnaya.telegrambot.AdminKeyboardMarkupFactory;
import com.sushnaya.telegrambot.Messages;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.state.admin.AdminBotDialogState;
import com.sushnaya.telegrambot.state.admin.AskCommandState;
import com.sushnaya.telegrambot.state.admin.AskLocalityState;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.sushnaya.telegrambot.Command.*;

public class MenuCreationDialog extends AdminBotDialogState<Menu> {
    private static final Messages MESSAGES = Messages.getDefaultMessages();
    private final AdminBotDialogState[] steps;
    private final AskLocalityState localityStep;
    private final AskCommandState localityConfirmationStep;
    private final CategoryCreationDialog categoryCreationDialog;
    private final ProductCreationDialog productCreationDialog;

    public MenuCreationDialog(final SushnayaBot bot,
                              final AdminKeyboardMarkupFactory keyboardMarkupFactory) {
        super(bot);

        steps = new AdminBotDialogState[]{
                localityStep = (AskLocalityState) new AskLocalityState(bot)
                        .setDefaultMessage(MESSAGES.askLocality())
                        .setHelpMessage(MESSAGES.askLocalityHelp() + "\n\n" +
                                MESSAGES.cancelMenuCreationHelp(CANCEL)),

                localityConfirmationStep = (AskCommandState) new AskCommandState(bot)
                        .setHelpMessage(MESSAGES.cancelMenuCreationHelp(CANCEL))
                        .setDefaultKeyboard(keyboardMarkupFactory.localityAlreadyBoundToMenuMarkup())
                        .ifThen(EDIT_LOCALITY, localityStep::ask)
                        .ifThen(BACK_TO_DASHBOARD, u -> {
                            bot.setAdminDefaultState(u);
                            dashboard(u);
                        }),

                categoryCreationDialog = new CategoryCreationDialog(bot, keyboardMarkupFactory),

                productCreationDialog = new ProductCreationDialog(bot, keyboardMarkupFactory)

        };

        categoryCreationDialog.getCategoryNameStep()
                .setDefaultKeyboard(keyboardMarkupFactory.editMenuLocalityMarkup())
                .ifThen(EDIT_LOCALITY, localityStep::ask);
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
        return MESSAGES.valueConfirmation(menu.getLocality().getDisplayName()) + " " +
                (bot.hasMenu() ? MESSAGES.askCategoryNameForMenuCreation() :
                        MESSAGES.askCategoryNameForFirstMenuCreation());
    }
}
