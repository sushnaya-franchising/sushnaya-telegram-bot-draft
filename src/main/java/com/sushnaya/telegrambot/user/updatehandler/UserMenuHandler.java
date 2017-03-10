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
            updateSelectedMenu(user, selectedMenu);

            askCategory(update, user);

        } else if (user.didSelectMenu()) {
            askCategory(update, user);

        } else {
            askMenu(update, user);
        }
    }

    public void updateSelectedMenu(User user, Menu selectedMenu) {
        user.setSelectedMenu(selectedMenu);
        bot.getDataStorage().saveUser(user);
    }

    private Menu getSelectedMenu(Update update) {
        final Integer menuId = parseCommandUriIntPayload(update);

        return bot.getDataStorage().getMenu(menuId);
    }

    private void askCategory(Update update, User user) {
        Menu menu = user.getSelectedMenu();

        if (!bot.hasPublishedProducts(menu.getId())) {
            askMenu(update, user);
            return;
        }

        say(update, MESSAGES.userMenuDefaultMessage(menu),
                bot.getKeyboardFactory(user).menuCategoriesKeyboard(
                        menu.getCategoriesWithPublishedProducts()));
    }

    private void askMenu(Update update, User user) {
        List<Menu> menus = bot.getMenusWithPublishedProducts();

        if (menus == null || menus.isEmpty()) {
            bot.say(update, MESSAGES.noProductsUserMessage(), true);

        } else if (menus.size() == 1) {
            updateSelectedMenu(user, menus.get(0));

            askCategory(update, user);

        } else {
            say(update, MESSAGES.selectMenu(),
                    bot.getKeyboardFactory(user).menusKeyboard(menus));
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
