package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import com.sushnaya.telegrambot.SushnayaBot;
import org.telegram.telegrambots.api.objects.Update;

import java.nio.ByteBuffer;

import static com.sushnaya.telegrambot.Command.parseCommandUriByteBufferPayload;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static java.lang.String.format;

public class RecoverCategoryHandler extends EditMenuHandler {
    public RecoverCategoryHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final ByteBuffer payload = parseCommandUriByteBufferPayload(update);

        final Menu menu = getMenu(payload);
        final Integer categoryId = getCategoryId(payload);

        if (menu == null && categoryId == null) return;

        final MenuCategory category = recoverCategory(categoryId);
        answer(update, menu, category);
    }

    private Menu getMenu(ByteBuffer payload) {
        return payload == null || payload.remaining() < 4 ? null :
                bot.getDataStorage().getMenu(payload.getInt());
    }

    private Integer getCategoryId(ByteBuffer payload) {
        return payload == null || payload.remaining() < 4 ? null : payload.getInt();
    }

    private MenuCategory recoverCategory(int categoryId) {
        return bot.getDataStorage().recoverCategory(categoryId);
    }

    private void answer(Update update, Menu menu, MenuCategory category) {
        final String message = format("%s\n\n%s", getEditMenuMessageText(menu),
                category == null ? MESSAGES.categoryRecoveryFailed() :
                        MESSAGES.categoryWasRecovered(category));

        bot.answer(update, message, getEditMenuKeyboard(menu));
    }
}
