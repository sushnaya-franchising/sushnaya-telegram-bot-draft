package com.sushnaya.telegrambot.user.updatehandler;

import com.sushnaya.entity.MenuCategory;
import com.sushnaya.entity.Product;
import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.DataStorage;
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
import static java.lang.String.format;

public class UserNextProductInCategoryHandler extends SushnayaBotUpdateHandler {
    private static final int PROGRESS_BAR_MAX_SIZE = 53;

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
        final DataStorage storage = bot.getDataStorage();
        final int productsCount = storage.getCategoryPublishedProductsCount(
                product.getCategory().getId());
        final int categoryId = product.getCategory().getId();
        final double progressPct = getProgressPct(cursor, productsCount);
        final String progressStr = getProgressStr(progressPct);
        final String heading = format("<b>%s</b>", product.getDisplayNameWithPrice(
                MESSAGES.getLocale()));
        final String subheading = product.getSubheading();
        final String message = (subheading == null ? heading :
                format("%s\n\n%s", heading, subheading)) + "\n" + progressStr;
        final InlineKeyboardMarkup keyboard = getNextProductKeyboard(
                categoryId, cursor, productsCount);

        if (product.hasTelegramPhotoFile()) {
            sendPhoto(bot, new SendPhoto()
                    .setChatId(getChatId(update))
                    .setPhoto(product.getTelegramPhotoFileId()));
        }

        bot.say(update, message, keyboard);
    }

    private void sendPhoto(SushnayaBot bot, SendPhoto sendPhoto) {
        try {
            bot.sendPhoto(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup getNextProductKeyboard(int categoryId, int cursor, int productsCount) {
        final int nextProductCursor = cursor + 1;

        if (nextProductCursor >= productsCount) {
            return KeyboardMarkupUtil.singleButtonInlineKeyboard(MESSAGES.menu(), MENU.getUri());

        } else {
            final String commandUri = buildCommandUri(NEXT_PRODUCT_IN_CATEGORY,
                    categoryId, nextProductCursor);

            return KeyboardMarkupUtil.singleButtonInlineKeyboard(MESSAGES.more(), commandUri);
        }
    }

    private String getProgressStr(double progressPct) {
        final int progressMax = PROGRESS_BAR_MAX_SIZE - 2;
        final int progressSymbols = (int) Math.ceil(progressPct * progressMax / 100.);
        final StringBuilder result = new StringBuilder().append("<b>.</b>");

        for (int i = 0; i < progressMax; i++) {
            result.append(i < progressSymbols ? '.' : ' ');
        }

        return result.append("<b>.</b>").toString();
    }

    private double getProgressPct(int cursor, double productsCount) {
        return 100 * (double) (cursor + 1) / productsCount;
    }
}
