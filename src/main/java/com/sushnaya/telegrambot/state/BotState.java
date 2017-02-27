package com.sushnaya.telegrambot.state;

import com.sushnaya.telegrambot.KeyboardMarkupFactory;
import com.sushnaya.telegrambot.Messages;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.Command;
import org.telegram.telegrambots.api.objects.Update;

public abstract class BotState {
    public static final Messages MESSAGES = Messages.getDefaultMessages();
    protected final SushnayaBot bot;

    public BotState(SushnayaBot bot) {
        this.bot = bot;
    }

    protected abstract boolean isSkippable();

    protected abstract boolean isCancellable();

    public final void handle(Update update) {
        switch (Command.parse(update)) {
            case START:
                if (isCancellable()) cancel(update);
                start(update);
                break;
            case HOME:
                if (isCancellable()) cancel(update);
                home(update);
                break;
            case HELP:
                help(update);
                break;
            case SKIP:
                handleSkip(update);
                break;
            case CANCEL:
                handleCancel(update);
                break;
            default:
                handleUpdate(update);
                break;
        }
    }

    private void handleCancel(Update update) {
        if (isCancellable()) {
            cancel(update);

        } else {
            nothingToCancel(update);
        }
    }

    protected void nothingToCancel(Update update) {
        bot.say(update, MESSAGES.nothingToCancel());
    }

    private void handleSkip(Update update) {
        if (isSkippable()) {
            skip(update);

        } else {
            nothingToSkip(update);
        }
    }

    protected void nothingToSkip(Update update) {
        bot.say(update, MESSAGES.nothingToSkip());
    }

    public abstract void start(Update update);

    public abstract void home(Update update);

    public abstract void help(Update update);

    public void skip(Update update) {
        throw new UnsupportedOperationException();
    }

    public void cancel(Update update) {
        throw new UnsupportedOperationException();
    }

    protected abstract void handleUpdate(Update update);

    public abstract KeyboardMarkupFactory getKeyboardMarkupFactory();
}
