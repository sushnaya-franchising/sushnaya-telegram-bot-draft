package com.sushnaya.telegrambot.state.user;

import com.sushnaya.telegrambot.DefaultKeyboardMarkupFactory;
import com.sushnaya.telegrambot.KeyboardMarkupFactory;
import com.sushnaya.telegrambot.Messages;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.state.BotState;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.Command.*;

public class UserDefaultState extends BotState {
    public static final Messages MESSAGES = Messages.getDefaultMessages();
    private final KeyboardMarkupFactory keyboardMarkupFactory = new DefaultKeyboardMarkupFactory();

    public UserDefaultState(SushnayaBot bot) {
        super(bot);
    }

    protected boolean isSkippable() {
        return false;
    }

    protected boolean isCancellable() {
        return false;
    }

    public KeyboardMarkupFactory getKeyboardMarkupFactory() {
        return keyboardMarkupFactory;
    }

    public void start(Update update) {
        bot.say(update, getGreetingMessageText(), true);

        home(update);
    }

    protected String getGreetingMessageText() {
        return MESSAGES.greeting();
    }

    public void home(Update update) {
        if (!bot.hasPublishedProducts()) {
            bot.say(update, getHomeMessageText(), true);
            return;
        }

        if (update.hasCallbackQuery()) {
            bot.edit(update, getHomeMessageText(), getKeyboardMarkupFactory().homeMarkup());

        } else {
            bot.say(update, getHomeMessageText(), getKeyboardMarkupFactory().homeMarkup());
        }
    }

    protected String getHomeMessageText() {
        return bot.hasProducts() ?
                MESSAGES.userHomeDefaultMessage() :
                MESSAGES.userHomeNoProductsMessage();
    }

    public void help(Update update) {
        bot.say(update, getHelpMessageText(), true);
    }

    protected String getHelpMessageText() {
        return MESSAGES.userHelp(HOME, HELP, SKIP, CANCEL);
    }

    protected void handleUpdate(Update update) {
        bot.say(update, MESSAGES.userUnknownCommand(HELP));
    }
}
