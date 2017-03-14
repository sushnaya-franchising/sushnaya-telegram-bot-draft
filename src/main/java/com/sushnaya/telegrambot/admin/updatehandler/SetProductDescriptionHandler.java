package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.Product;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.admin.state.AdminBotDialogState;
import com.sushnaya.telegrambot.admin.state.AskTextState;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.singleButtonOneTimeKeyboard;

public class SetProductDescriptionHandler extends EditProductHandler {

    public SetProductDescriptionHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final Product product = getProduct(update);

        if (product != null) {
            askProductDescription(update, product)
                    .then((u, description) -> updateProductDescription(u, product, description))
                    .ifThen(DELETE_OPTIONAL_PROPERTY, u -> deleteProductDescription(u, product))
                    .onCancel(u -> adjustBotState(u, product));

        } else {
            bot.handleUnknownCommand(update);
        }
    }

    public void adjustBotState(Update u, Product product) {
        bot.setAdminDefaultState(u);
        editProduct(u, product);
    }

    private void updateProductDescription(Update update, Product product, String description) {
        updateProductDescription(product, description);

        bot.say(update, MESSAGES.productDescriptionUpdateSucceeded(), true);

        adjustBotState(update, product);
    }

    private void updateProductDescription(Product product, String description) {
        product.setDescription(description);
        bot.getDataStorage().saveProduct(product);
    }

    private void deleteProductDescription(Update update, Product product) {
        updateProductDescription(update, product, null);
    }

    public AdminBotDialogState<String> askProductDescription(Update update, Product product) {
        final ReplyKeyboardMarkup keyboard = product.hasDescription() ?
                singleButtonOneTimeKeyboard(MESSAGES.deleteProductDescription()) : null;

        return new AskTextState(bot).ask(update, MESSAGES.askProductDescription(), keyboard)
                .setExtraCommandParser((u) -> {
                    if (u.hasMessage() && u.getMessage().hasText()) {
                        final String text = u.getMessage().getText();

                        if (text.equalsIgnoreCase(MESSAGES.deleteProductDescription())) {
                            return DELETE_OPTIONAL_PROPERTY;
                        }
                    }

                    return NOP;
                });
    }

    private Product getProduct(Update update) {
        Integer productId = parseCommandUriIntPayload(update);

        return productId == null ? null : bot.getDataStorage().getProduct(productId);
    }
}
