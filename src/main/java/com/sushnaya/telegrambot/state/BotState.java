package com.sushnaya.telegrambot.state;

import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.KeyboardMarkupFactory;
import com.sushnaya.telegrambot.Messages;
import com.sushnaya.telegrambot.SushnayaBot;
import org.telegram.telegrambots.api.objects.Update;

// todo: all bot states must be stateless singletons
public abstract class BotState {
    public static final Messages MESSAGES = Messages.getDefaultMessages();
    protected final SushnayaBot bot;

    public BotState(SushnayaBot bot) {
        this.bot = bot;
    }

    public boolean handle(Update update) {
        switch (Command.parseCommand(update)) {
            case START:
                start(update);
                return true;
            case MENU:
            case BACK_TO_MENU:
                menu(update);
                return true;
            case HELP:
                help(update);
                return true;
            case SKIP:
                skip(update);
                return true;
            case CANCEL:
                cancel(update);
                return true;
            default:
                return false;
        }
    }

    public abstract void start(Update update);

    public abstract void menu(Update update);

    public abstract void help(Update update);

    public void skip(Update update) {
        sayNothingToSkip(update);
    }

    private void sayNothingToSkip(Update update) {
        bot.say(update, MESSAGES.nothingToSkip());
    }

    public void cancel(Update update) {
        sayNothingToCancel(update);
    }

    private void sayNothingToCancel(Update update) {
        bot.say(update, MESSAGES.nothingToCancel());
    }

    public abstract KeyboardMarkupFactory getKeyboardMarkupFactory();
}
