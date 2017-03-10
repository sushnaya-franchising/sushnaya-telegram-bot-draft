package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.Menu;
import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import org.telegram.telegrambots.api.objects.Update;

import java.util.List;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.selectMenuKeyboard;

public class EditMenuHandler extends SushnayaBotUpdateHandler {

    public EditMenuHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final Menu menu = getMenu(update);

        if (menu == null) {
            askMenuToEdit(update);

        } else {
            editMenu(update, menu);
        }
    }

    private Menu getMenu(Update update) {
        Integer menuId = Command.parseCommandUriIntPayload(update);

        return bot.getDataStorage().getMenu(menuId);
    }

    private void askMenuToEdit(Update update) {
        final List<Menu> menus = bot.getDataStorage().getMenus();

        if (menus == null || menus.isEmpty()) {
            bot.say(update, MESSAGES.noMenuWasCreatedToEdit(CREATE_MENU),
                    true);

        } else if (menus.size() == 1) {
            editMenu(update, menus.get(0));

        } else {
            bot.answer(update, MESSAGES.selectMenuToEdit(),
                    selectMenuKeyboard(menus, m -> buildCommandUri(EDIT_MENU, m.getId())));
        }
    }

    private void editMenu(Update update, Menu menu) {
        bot.answer(update, MESSAGES.menuSettings(menu),
                bot.getAdminKeyboardFactory().editMenu(menu));
    }
}
