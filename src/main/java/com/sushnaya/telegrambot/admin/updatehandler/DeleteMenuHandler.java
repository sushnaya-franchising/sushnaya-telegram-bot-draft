package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.Menu;
import com.sushnaya.telegrambot.SushnayaBot;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;

import static com.sushnaya.telegrambot.Command.RECOVER_MENU;
import static com.sushnaya.telegrambot.Command.buildCommandUri;
import static com.sushnaya.telegrambot.Command.parseCommandUriIntPayload;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.addFirstButton;
import static java.lang.String.format;

public class DeleteMenuHandler extends DashboardHandler {

    public DeleteMenuHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final Menu menu = getMenu(update);

        if (menu != null) {
            deleteMenu(menu);

            answer(update, menu);
        }
    }

    private Menu getMenu(Update update) {
        Integer menuId = parseCommandUriIntPayload(update);

        return menuId == null ? null: bot.getDataStorage().getMenu(menuId);
    }

    private void deleteMenu(Menu menu) {
        bot.getDataStorage().deleteMenu(menu);
    }

    private void answer(Update update, Menu menu) {
        final String message = format("%s\n\n%s", getDashboardMessageText(),
                MESSAGES.menuWasDeleted(menu));
        final InlineKeyboardMarkup keyboard = addFirstButton(getDashboardKeyboard(),
                MESSAGES.recoverMenu(menu), buildCommandUri(RECOVER_MENU, menu.getId()));

        bot.answer(update, message, keyboard);
    }
}
