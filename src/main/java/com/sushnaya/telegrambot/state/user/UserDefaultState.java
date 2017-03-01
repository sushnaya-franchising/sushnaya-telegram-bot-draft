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
        bot.menu(update);
    }

    protected String getGreetingMessageText() {
        return MESSAGES.greeting();
    }

    public void menu(Update update) {
        final User user = bot.getDataStorage().getUserByTelegramId(
                getTelegramUserId(update));

        handleIfMenuIsSelected(update, user);

        if (user.didSelectMenu()) {
            revealMenu(update, user.getSelectedMenu());

        } else {
            askMenu(update);
        }
    }

    private void revealMenu(Update update, Menu menu) {
        if (!bot.hasPublishedProducts(menu.getId())) {
            askMenu(update);
            return;
        }

        say(update, MESSAGES.userMenuDefaultMessage(),
                getKeyboardMarkupFactory().menu(menu));
    }

    private void handleIfMenuIsSelected(Update update, User user) {
        final Integer menuId = Command.parseId(update);
        if (menuId != null) {
            final Menu menu = bot.getDataStorage().getMenu(menuId);
            user.setSelectedMenu(menu);
            bot.getDataStorage().saveUser(user);
        }
    }

    private void askMenu(Update update) {
        List<Menu> menus = bot.getMenusWithPublishedProducts();

        if (menus == null || menus.isEmpty()) {
            bot.say(update, MESSAGES.userMenuNoProductsMessage(), true);

        } else if (menus.size() == 1) {
            revealMenu(update, menus.get(0));

        } else {
            say(update, MESSAGES.selectMenu(), getKeyboardMarkupFactory().selectMenu(menus));
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
        // todo: add change city command if more than 1 menu
        bot.say(update, getHelpMessageText(), true);
    }

    protected String getHelpMessageText() {
        return MESSAGES.userHelp(MENU, HELP, SKIP, CANCEL);
    }
}
