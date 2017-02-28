package com.sushnaya.telegrambot.state.user;

import com.sushnaya.entity.Menu;
import com.sushnaya.entity.User;
import com.sushnaya.telegrambot.*;
import com.sushnaya.telegrambot.state.BotState;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.util.UpdateUtil.getTelegramUserId;

public class UserDefaultState extends BotState {
    public static final Messages MESSAGES = Messages.getDefaultMessages();
    private final KeyboardMarkupFactory keyboardMarkupFactory = new DefaultKeyboardMarkupFactory();

    public UserDefaultState(SushnayaBot bot) {
        super(bot);
    }

    public KeyboardMarkupFactory getKeyboardMarkupFactory() {
        return keyboardMarkupFactory;
    }

    public void start(Update update) {
        bot.say(update, getGreetingMessageText(), true);

        menu(update);
    }

    protected String getGreetingMessageText() {
        return MESSAGES.greeting();
    }

    public void menu(Update update) {
        final User user = bot.getDataStorage().getUserByTelegramId(
                getTelegramUserId(update));
        assert user != null;

        handleIfMenuIsSelected(update, user);

        final Menu selectedMenu = user.getSelectedMenu();

        if (selectedMenu != null && bot.hasPublishedProducts(selectedMenu.getId())) {
            revealMenu(update, selectedMenu);

        } else {
            revealMenus(update);
        }
    }

    private void handleIfMenuIsSelected(Update update, User user) {
        final Integer menuId = Command.parseId(update);
        if (menuId != null) {
            final Menu menu = bot.getDataStorage().getMenu(menuId);
            user.setSelectedMenu(menu);
            bot.getDataStorage().saveUser(user);
        }
    }

    private void revealMenu(Update update, Menu menu) {
        final InlineKeyboardMarkup keyboardMarkup =
                getKeyboardMarkupFactory().menuMarkup(menu.getMenuCategories());

        say(update, MESSAGES.userMenuDefaultMessage(), keyboardMarkup);
    }

    private void revealMenus(Update update) {
        List<Menu> menus = bot.getMenusWithPublishedProducts();

        if (menus == null || menus.isEmpty()) {
            bot.say(update, MESSAGES.userMenuNoProductsMessage(), true);

        } else if (menus.size() == 1) {
            revealMenu(update, menus.get(0));

        } else {
            say(update, MESSAGES.selectLocality(),
                    getKeyboardMarkupFactory().menusMarkup(menus));
        }
    }

    private void say(Update update, String message, InlineKeyboardMarkup keyboardMarkup) {
        if (update.hasCallbackQuery()) {
            bot.edit(update, message, keyboardMarkup);

        } else {
            bot.say(update, message, keyboardMarkup);
        }
    }

    public void help(Update update) {
        bot.say(update, getHelpMessageText(), true);
    }

    protected String getHelpMessageText() {
        return MESSAGES.userHelp(MENU, HELP, SKIP, CANCEL);
    }
}
