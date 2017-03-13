package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.Menu;
import com.sushnaya.telegrambot.SushnayaBot;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.Command.parseCommandUriIntPayload;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static java.lang.String.format;

public class RestoreMenuHandler extends DashboardHandler {
    public RestoreMenuHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final Integer menuId = parseCommandUriIntPayload(update);

        if (menuId != null) {
            recoverMenu(menuId);

            answer(update, bot.getDataStorage().getMenu(menuId));
        }
    }

    private void recoverMenu(int menuId) {
        bot.getDataStorage().recoverMenu(menuId);
    }

    private void answer(Update update, Menu menu) {
        final String message = format("%s\n\n%s", getDashboardMessageText(),
                MESSAGES.menuWasRecovered(menu));

        bot.answer(update, message, getDashboardKeyboard());
    }
}
