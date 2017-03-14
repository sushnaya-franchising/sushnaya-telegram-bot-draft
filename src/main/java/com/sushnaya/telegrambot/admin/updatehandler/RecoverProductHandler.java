package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.MenuCategory;
import com.sushnaya.entity.Product;
import com.sushnaya.telegrambot.SushnayaBot;
import org.telegram.telegrambots.api.objects.Update;

import java.nio.ByteBuffer;

import static com.sushnaya.telegrambot.Command.parseCommandUriByteBufferPayload;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static java.lang.String.format;

public class RecoverProductHandler extends EditCategoryHandler {
    public RecoverProductHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final ByteBuffer payload = parseCommandUriByteBufferPayload(update);
        final MenuCategory category = getMenuCategory(payload);
        final Integer productId = getProductId(payload);

        if (category != null && productId != null){
            final Product product = recoverProduct(productId);
            answer(update, category, product);

        } else {
            bot.handleUnknownCommand(update);
        }
    }

    private Product recoverProduct(int productId) {
        return bot.getDataStorage().recoverProduct(productId);
    }

    private MenuCategory getMenuCategory(ByteBuffer payload) {
        return payload == null || payload.remaining() < 4 ? null :
                bot.getDataStorage().getMenuCategory(payload.getInt());
    }

    private Integer getProductId(ByteBuffer payload) {
        return payload == null || payload.remaining() < 4 ? null : payload.getInt();
    }

    private void answer(Update update, MenuCategory category, Product product) {
        final String message = format("%s\n\n%s", getEditCategoryMessageText(category),
                product == null ? MESSAGES.productRecoveryFailed() :
                        MESSAGES.productWasRecovered(product));

        bot.answer(update, message, getEditCategoryKeyboard(category));
    }

}
