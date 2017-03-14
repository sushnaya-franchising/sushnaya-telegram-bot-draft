package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.Product;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.admin.state.AdminBotDialogState;
import com.sushnaya.telegrambot.admin.state.AskDoubleValueState;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.Command.parseCommandUriIntPayload;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;

public class SetProductPriceHandler extends EditProductHandler {
    public SetProductPriceHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final Product product = getProduct(update);

        if (product != null) {
            askProductPrice(update).then((u, price) -> updateProductPrice(u, product, price))
                    .onCancel(u -> adjustBotState(u, product));

        } else {
            bot.handleUnknownCommand(update);
        }
    }

    public void updateProductPrice(Update u, Product product, Double price) {
        updateProductPrice(product, price);

        bot.say(u, MESSAGES.productPriceUpdateSucceeded(), true);

        adjustBotState(u, product);
    }

    public void adjustBotState(Update u, Product product) {
        bot.setAdminDefaultState(u);
        editProduct(u, product);
    }

    public void updateProductPrice(Product product, Double price) {
        product.setPrice(price);
        bot.getDataStorage().saveProduct(product);
    }

    public AdminBotDialogState<Double> askProductPrice(Update update) {
        return new AskDoubleValueState(bot).ask(update, MESSAGES.askProductPrice(), true);
    }

    private Product getProduct(Update update) {
        Integer productId = parseCommandUriIntPayload(update);

        return productId == null ? null : bot.getDataStorage().getProduct(productId);
    }
}
