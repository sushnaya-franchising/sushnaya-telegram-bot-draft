package com.sushnaya.telegrambot.user.updatehandler;

import com.sushnaya.entity.Menu;
import com.sushnaya.entity.User;
import com.sushnaya.telegrambot.DataStorage;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

import static com.sushnaya.telegrambot.Command.parseCommandUriIntPayload;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.selectCategoryKeyboard;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.selectMenuKeyboard;
import static com.sushnaya.telegrambot.util.UpdateUtil.getTelegramUserId;

public class UserMenuHandler extends SushnayaBotUpdateHandler {

    public UserMenuHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final DataStorage storage = bot.getDataStorage();
        final User user = storage.getUserByTelegramId(getTelegramUserId(update));
        final Menu selectedMenu = getSelectedMenu(update);

        if (selectedMenu != null) {
            user.setSelectedMenu(selectedMenu);
            storage.saveUser(user);

            askCategory(update, selectedMenu);

        } else if (user.didSelectMenu()) {
            askCategory(update, user.getSelectedMenu());

        } else {
            askMenu(update);
        }
    }

    private Menu getSelectedMenu(Update update) {
        final Integer menuId = parseCommandUriIntPayload(update);

        return bot.getDataStorage().getMenu(menuId);
    }

    private void askCategory(Update update, Menu menu) {
        if (!bot.hasPublishedProducts(menu.getId())) {
            askMenu(update);
            return;
        }

        say(update, MESSAGES.userMenuDefaultMessage(menu),
                selectCategoryKeyboard(menu.getCategoriesWithPublishedProducts()));
    }

    private void askMenu(Update update) {
        List<Menu> menus = bot.getMenusWithPublishedProducts();

        if (menus == null || menus.isEmpty()) {
            bot.say(update, MESSAGES.noProductsUserMessage(), true);

        } else if (menus.size() == 1) {
            askCategory(update, menus.get(0));

        } else {
            say(update, MESSAGES.selectMenu(), selectMenuKeyboard(menus));
        }
    }

    private void say(Update update, String message, InlineKeyboardMarkup keyboardMarkup) {
        if (update.hasCallbackQuery() && !update.getCallbackQuery().getMessage().hasPhoto()) {
            bot.edit(update, message, keyboardMarkup);

        } else {
            bot.say(update, message, keyboardMarkup);
        }
    }
}
