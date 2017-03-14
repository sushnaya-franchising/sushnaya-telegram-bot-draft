package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.Product;
import com.sushnaya.telegrambot.SushnayaBot;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.Command.parseCommandUriIntPayload;

public class PublishProductHandler extends EditProductHandler {
    public PublishProductHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final Product product = getProduct(update);

        if (product != null) {
            product.setPublished(true);

            bot.getDataStorage().saveProduct(product);

            editProduct(update, product);

        } else {
            bot.handleUnknownCommand(update);
        }
    }

    private Product getProduct(Update update) {
        Integer productId = parseCommandUriIntPayload(update);

        return productId == null ? null : bot.getDataStorage().getProduct(productId);
    }
}
