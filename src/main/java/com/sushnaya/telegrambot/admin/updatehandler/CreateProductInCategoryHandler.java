package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.MenuCategory;
import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import com.sushnaya.telegrambot.admin.state.dialog.ProductCreationDialog;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;

// todo: handle cancel command?
public class CreateProductInCategoryHandler extends SushnayaBotUpdateHandler {
    private ProductCreationDialog productCreationDialog;

    public CreateProductInCategoryHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        createProductInCategory(update, getCategory(update));
    }

    private MenuCategory getCategory(Update update) {
        final Integer categoryId = Command.parseCommandUriIntPayload(update);

        return bot.getDataStorage().getMenuCategory(categoryId);
    }

    void createProductInCategory(Update update, MenuCategory category) {
        if (category == null) {
            bot.handleCommand(update, CREATE_PRODUCT);
            return;
        }

        ensureProductCreationDialog().ask(update).then((u, product) -> {
            bot.setAdminDefaultState(u);

            category.addProduct(product);
            bot.getDataStorage().saveCategory(category);

            bot.say(u, MESSAGES.productCreationIsSuccessful(product), true);
            bot.say(u, MESSAGES.proposeFurtherCommandsForProductCreation(),
                    bot.getAdminKeyboardFactory().productCreationFurtherCommands(
                            category.getMenu(), category));
        }).onCancel(this::cancelProductCreation);
    }

    private ProductCreationDialog ensureProductCreationDialog() {
        return productCreationDialog != null ? productCreationDialog :
                (productCreationDialog = new ProductCreationDialog(bot));
    }

    void cancelProductCreation(Update u) {
        bot.setAdminDefaultState(u);

        final Command command = Command.parseCommand(u);

        if (command == CREATE_PRODUCT || command == CREATE_PRODUCT_IN_CATEGORY) return;

        String message = command == CANCEL ? MESSAGES.productCreationIsCancelled(HELP) :
                MESSAGES.productCreationIsInterrupted(HELP);

        bot.say(u, message, true);
    }
}
