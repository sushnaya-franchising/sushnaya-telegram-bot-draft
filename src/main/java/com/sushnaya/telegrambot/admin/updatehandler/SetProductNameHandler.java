package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.Product;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.admin.state.AdminBotDialogState;
import com.sushnaya.telegrambot.admin.state.AskTextState;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.Command.parseCommandUriIntPayload;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;

public class SetProductNameHandler extends EditProductHandler {
    public SetProductNameHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final Product product = getProduct(update);

        if (product != null) {
            askProductName(update).then((u, name) -> updateProductName(u, product, name))
                    .onCancel(u -> adjustBotState(u, product));

        } else {
            bot.handleUnknownCommand(update);
        }
    }

    private void updateProductName(Update u, Product product, String name) {
        updateProductName(product, name);

        bot.say(u, MESSAGES.productNameUpdateSucceeded(), true);

        adjustBotState(u, product);
    }

    private void adjustBotState(Update u, Product product) {
        bot.setAdminDefaultState(u);
        editProduct(u, product);
    }

    private void updateProductName(Product product, String name) {
        product.setName(name);
        bot.getDataStorage().saveProduct(product);
    }

    private AdminBotDialogState<String> askProductName(Update update) {
        return new AskTextState(bot).ask(update, MESSAGES.askProductName(), true);
    }

    private Product getProduct(Update update) {
        Integer productId = parseCommandUriIntPayload(update);

        return productId == null ? null : bot.getDataStorage().getProduct(productId);
    }
}
