package com.sushnaya.telegrambot.state.admin;

import com.google.common.collect.Maps;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.Command;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.sushnaya.telegrambot.KeyboardMarkupFactory.REPLY_KEYBOARD_REMOVE;
import static com.sushnaya.telegrambot.Command.HELP;

public abstract class AdminBotDialogState<R> extends AdminDefaultState {
    private Consumer<Update> onCancel;
    private BiConsumer<Update, R> then;
    private String defaultMessage;
    private ReplyKeyboard defaultKeyboard;
    private String helpMessage;
    private Map<Command, Consumer<Update>> thenByCommand;
    private boolean isSkippable;

    public AdminBotDialogState(SushnayaBot bot) {
        super(bot);
    }

    protected boolean isCancellable() {
        return true;
    }

    @Override
    protected boolean isSkippable() {
        return isSkippable;
    }

    public AdminBotDialogState<R> setSkippable(boolean skippable) {
        isSkippable = skippable;
        return this;
    }

    public AdminBotDialogState<R> onCancel(Consumer<Update> onCancel) {
        this.onCancel = onCancel;
        return this;
    }

    protected Consumer<Update> getOnCancel() {
        return onCancel;
    }

    public AdminBotDialogState<R> setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
        return this;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public AdminBotDialogState<R> setDefaultKeyboard(ReplyKeyboard defaultKeyboard) {
        this.defaultKeyboard = defaultKeyboard;
        return this;
    }

    public AdminBotDialogState<R> setHelpMessage(String helpMessage) {
        this.helpMessage = helpMessage;
        return this;
    }

    public AdminBotDialogState<R> ifThen(Command command, Consumer<Update> then) {
        if (thenByCommand == null) thenByCommand = Maps.newHashMap();
        thenByCommand.put(command, then);
        return this;
    }

    public AdminBotDialogState<R> then(BiConsumer<Update, R> then) {
        this.then = then;
        return this;
    }

    protected BiConsumer<Update, R> getThen() {
        return then;
    }

    protected final void handleUpdate(Update update) {
        Consumer<Update> commandThen = getCommandThen(update);
        
        if (commandThen != null) {
            commandThen.accept(update);

        } else {
            handleUpdate(update, then, this::ask);
        }
    }

    private Consumer<Update> getCommandThen(Update update) {
        if (thenByCommand == null) return null;

        return thenByCommand.get(Command.parse(update));
    }

    protected abstract void handleUpdate(Update update, BiConsumer<Update, R> ok,
                                         BiConsumer<Update, String> ko);

    public void help(Update update) {
        if (helpMessage != null) {
            bot.say(update, helpMessage);
            ask(update);

        } else {
            super.help(update);
        }
    }

    public void skip(Update update) {
        if (then != null) {
            then.accept(update, null);

        } else {
            super.skip(update);
        }
    }

    public void cancel(Update update) {
        if (onCancel != null) {
            onCancel.accept(update);

        } else {
            super.cancel(update);
        }
    }

    protected void nothingToSkip(Update update) {
        super.nothingToSkip(update);
        ask(update);
    }

    public AdminBotDialogState<R> ask(Update update) {
        ask(update, null);
        return this;
    }

    public AdminBotDialogState<R> ask(Update update, boolean removeKeyboard) {
        ask(update, null, removeKeyboard);
        return this;
    }

    public AdminBotDialogState<R> ask(Update update, String message) {
        ask(update, message, null);
        return this;
    }

    public AdminBotDialogState<R> ask(Update update, String message, boolean removeKeyboard) {
        ask(update, message, removeKeyboard ? REPLY_KEYBOARD_REMOVE : null);
        return this;
    }

    public AdminBotDialogState<R> ask(Update update, String message, ReplyKeyboard keyboard) {
        if (message == null && defaultMessage == null) throw new IllegalStateException(
                "Default message must be not null");

        say(update, sanitizeMessage(message) + ' ' + HELP.getUri(), keyboard);
        bot.setState(update, this);

        return this;
    }

    private String sanitizeMessage(String message) {
        return message == null ? defaultMessage : message;
    }

    public void say(Update update, String message, ReplyKeyboard keyboard) {
        if (message == null && defaultMessage == null) throw new IllegalStateException(
                "Default message must be not null");

        if (defaultMessage == null) setDefaultMessage(message);
        if (defaultKeyboard == null) setDefaultKeyboard(keyboard);

        bot.say(update, sanitizeMessage(message),
                keyboard == null ? defaultKeyboard : keyboard);
    }
}

