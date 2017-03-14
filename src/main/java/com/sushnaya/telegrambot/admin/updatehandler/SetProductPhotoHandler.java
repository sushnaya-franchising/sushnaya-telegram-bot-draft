package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.Product;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.admin.state.AdminBotDialogState;
import com.sushnaya.telegrambot.admin.state.AskPhotoState;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.singleButtonOneTimeKeyboard;

public class SetProductPhotoHandler extends EditProductHandler {
    public SetProductPhotoHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final Product product = getProduct(update);

        if (product != null) {
            askProductPhoto(update, product)
                    .then((u, photoFileId) -> updateProductPhoto(u, product, photoFileId))
                    .ifThen(DELETE_OPTIONAL_PROPERTY, u -> deleteProductPhoto(u, product))
                    .onCancel(u -> adjustBotState(u, product));

        } else {
            bot.handleUnknownCommand(update);
        }
    }

    private void adjustBotState(Update u, Product product) {
        bot.setAdminDefaultState(u);
        editProduct(u, product);
    }

    private void updateProductPhoto(Update update, Product product, String photoFileId) {
        updateProductPhoto(product, photoFileId);

        bot.say(update, MESSAGES.productPhotoUpdateSucceeded(), true);

        adjustBotState(update, product);
    }

    private void updateProductPhoto(Product product, String photoFileId) {
        product.setTelegramPhotoFileId(photoFileId);
        bot.getDataStorage().saveProduct(product);
    }

    private void deleteProductPhoto(Update update, Product product) {
        updateProductPhoto(update, product, null);
    }

    public AdminBotDialogState<String> askProductPhoto(Update update, Product product) {
        final ReplyKeyboardMarkup keyboard = product.hasPhoto() ?
                singleButtonOneTimeKeyboard(MESSAGES.deleteProductPhoto()) : null;

        return new AskPhotoState(bot).ask(update, MESSAGES.askProductPhoto(), keyboard)
                .setExtraCommandParser((u) -> {
                    if (u.hasMessage() && u.getMessage().hasText()) {
                        final String text = u.getMessage().getText();

                        if (text.equalsIgnoreCase(MESSAGES.deleteProductPhoto())) {
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
