package com.sushnaya.telegrambot.user.updatehandler;

import com.sushnaya.entity.MenuCategory;
import com.sushnaya.entity.Product;
import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import com.sushnaya.telegrambot.util.KeyboardMarkupUtil;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.nio.ByteBuffer;
import java.util.List;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.UpdateUtil.getChatId;

public class UserNextProductInCategoryHandler extends SushnayaBotUpdateHandler {

    public static final int TELEGRAM_PHOTO_CAPTION_MAX_LENGTH = 200;

    public UserNextProductInCategoryHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final ByteBuffer payload = Command.parseCommandUriByteBufferPayload(update);

        if (payload == null) return;

        final int categoryId = payload.getInt();
        final int cursor = payload.remaining() == 4 ? payload.getInt() : 0;
        final MenuCategory category = bot.getDataStorage().getMenuCategory(categoryId);

        if (category == null) return;

        showProduct(update, category, cursor);
    }

    private void showProduct(Update update, MenuCategory category, int cursor) {
        final List<Product> products = bot.getDataStorage().getPublishedProducts(
                category.getId(), cursor, 1);

        if (products == null || products.isEmpty()) {
            bot.revealMenu(update);
        }

        showProduct(update, products.get(0), cursor);
    }

    private void showProduct(Update update, Product product, int cursor) {
        final int nextProductCursor = cursor + 1;
        final int categoryId = product.getMenuCategory().getId();
        final int productsCount = bot.getDataStorage()
                .getCategoryPublishedProductsCount(categoryId);
        final String displayName = product.getDisplayName(MESSAGES.getLocale());
        final String subheading = product.getSubheading();
        final String message = subheading != null ? displayName + "\n\n" + subheading : displayName;
        final InlineKeyboardMarkup keyboard = productKeyboard(
                categoryId, nextProductCursor, productsCount);

        if (product.hasTelegramPhotoFile()) {
            SendPhoto photo = new SendPhoto()
                    .setChatId(getChatId(update))
                    .setPhoto(product.getTelegramPhotoFileId());

            if (message.length() <= TELEGRAM_PHOTO_CAPTION_MAX_LENGTH) {
                sendPhoto(bot, photo.setCaption(message)
                        .setReplyMarkup(keyboard));

            } else {
                sendPhoto(bot, photo);
                bot.say(update, message, keyboard);
            }

        } else {
            bot.say(update, message, keyboard);
        }
    }

    private void sendPhoto(SushnayaBot bot, SendPhoto sendPhoto) {
        try {
            bot.sendPhoto(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup productKeyboard(
            int categoryId, int nextProductCursor, int productsCount) {
        if (nextProductCursor > productsCount) throw new IndexOutOfBoundsException();

        if (nextProductCursor == productsCount) {
            return KeyboardMarkupUtil.singleButtonInlineKeyboard(MESSAGES.menu(), MENU.getUri());

        } else {
            final String buttonText = MESSAGES.nextProduct(
                    nextProductCursor + 1, productsCount);
            final ByteBuffer commandUriPayload = ByteBuffer.allocate(8)
                    .putInt(categoryId).putInt(nextProductCursor);
            final String commandUri = buildCommandUri(NEXT_PRODUCT_IN_CATEGORY, commandUriPayload);

            return KeyboardMarkupUtil.singleButtonInlineKeyboard(buttonText, commandUri);
        }
    }
}
