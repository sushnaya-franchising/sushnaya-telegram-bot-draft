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

public class SetProductSubheadingHandler extends EditProductHandler {
    public SetProductSubheadingHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final Product product = getProduct(update);

        if (product != null) {
            askProductSubheading(update, product)
                    .then((u, subheading) -> updateProductSubheading(u, product, subheading))
                    .ifThen(DELETE_OPTIONAL_PROPERTY, u -> deleteProductSubheading(u, product))
                    .onCancel(u -> adjustBotState(u, product));

        } else {
            bot.handleUnknownCommand(update);
        }
    }

    private void adjustBotState(Update u, Product product) {
        bot.setAdminDefaultState(u);
        editProduct(u, product);
    }

    private void deleteProductSubheading(Update u, Product product) {
        updateProductSubheading(u, product, null);
    }

    private void updateProductSubheading(Update u, Product product, String subheading) {
        updateProductSubheading(product, subheading);

        bot.say(u, MESSAGES.productSubheadingUpdateSucceeded(), true);

        adjustBotState(u, product);
    }

    private void updateProductSubheading(Product product, String subheading) {
        product.setSubheading(subheading);
        bot.getDataStorage().saveProduct(product);
    }

    private AdminBotDialogState<String> askProductSubheading(Update update, Product product) {
        final ReplyKeyboardMarkup keyboard = product.hasSubheading() ?
                singleButtonOneTimeKeyboard(MESSAGES.deleteProductSubheading()) : null;

        return new AskTextState(bot).ask(update, MESSAGES.askProductSubheading(), keyboard)
                .setExtraCommandParser((u) -> {
                    if (u.hasMessage() && u.getMessage().hasText()) {
                        final String text = u.getMessage().getText();

                        if (text.equalsIgnoreCase(MESSAGES.deleteProductSubheading())) {
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
