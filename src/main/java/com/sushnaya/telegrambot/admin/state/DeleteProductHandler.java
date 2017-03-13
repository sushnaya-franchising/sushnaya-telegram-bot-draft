package com.sushnaya.telegrambot.admin.state;

import com.sushnaya.entity.MenuCategory;
import com.sushnaya.entity.Product;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.admin.updatehandler.EditCategoryHandler;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.addFirstButton;
import static java.lang.String.format;

public class DeleteProductHandler extends EditCategoryHandler {

    public DeleteProductHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final Product product = getProduct(update);

        if (product != null) {
            final MenuCategory category = product.getCategory();

            deleteProduct(product);

            answer(update, category, product);
        }
    }

    private void deleteProduct(Product product) {
        bot.getDataStorage().deleteProduct(product);
        product.getCategory().removeProduct(product);// todo: danger! improve design
    }

    private Product getProduct(Update update) {
        Integer productId = parseCommandUriIntPayload(update);

        return productId == null ? null : bot.getDataStorage().getProduct(productId);
    }

    private void answer(Update update, MenuCategory category, Product product) {
        final String message = format("%s\n\n%s", getEditCategoryMessageText(category),
                MESSAGES.productWasDeleted(product));
        final InlineKeyboardMarkup keyboard = addFirstButton(getEditCategoryKeyboard(category),
                MESSAGES.recoverProduct(product), buildCommandUri(RECOVER_PRODUCT,
                        category.getId(), product.getId()));

        bot.answer(update, message, keyboard);
    }
}
