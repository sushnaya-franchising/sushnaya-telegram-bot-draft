package com.sushnaya.telegrambot.state.admin;

import com.google.common.collect.Maps;
import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.sushnaya.telegrambot.Command.HELP;
import static com.sushnaya.telegrambot.KeyboardMarkupFactory.REPLY_KEYBOARD_REMOVE;

public abstract class AdminBotDialogState<R> extends AdminDefaultState {
    private Consumer<Update> onCancel;
    private BiConsumer<Update, R> then;
    private String defaultMessage;
    private ReplyKeyboard defaultKeyboard;
    private String helpMessage;
    private Map<Command, Consumer<Update>> thenByCommand;
    private boolean skippable;

    public AdminBotDialogState(SushnayaBot bot) {
        super(bot);
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

    @Override
    public boolean handle(Update update) {
        if (super.handle(update)) return true;

        Consumer<Update> then = getThenForUpdate(update);
        if (then != null) {
            then.accept(update);

        } else {
            handleUpdate(update, this.then, this::ask);
        }

        return true;
    }

    private Consumer<Update> getThenForUpdate(Update update) {
        return getThenForCommand(Command.parseCommand(update));
    }

    private Consumer<Update> getThenForCommand(Command command) {
        if (thenByCommand == null) return null;

        return thenByCommand.get(command);
    }

    protected abstract void handleUpdate(Update update, BiConsumer<Update, R> ok,
                                         BiConsumer<Update, String> ko);

    @Override
    public void start(Update update) {
        cancel(update);
        super.start(update);
    }

    @Override
    public void menu(Update update) {
        cancel(update);
        super.menu(update);
    }

    @Override
    public void dashboard(Update update) {
        cancel(update);
        super.dashboard(update);
    }

    @Override
    public void editMenu(Update update) {
        cancel(update);
        super.editMenu(update);
    }

    @Override
    public void createMenu(Update update) {
        cancel(update);
        super.createMenu(update);
    }

    @Override
    public void createCategory(Update update, Menu menu) {
        cancel(update);
        super.createCategory(update, menu);
    }

    @Override
    public void createProductInMenu(Update update, Menu menu) {
        cancel(update);
        super.createProductInMenu(update, menu);
    }

    @Override
    public void createProductInCategory(Update update, MenuCategory category) {
        cancel(update);
        super.createProductInCategory(update, category);
    }

    public void help(Update update) {
        if (helpMessage != null) {
            bot.say(update, helpMessage);
            ask(update);

        } else {
            super.help(update);
        }
    }

    public void skip(Update update) {
        if (skippable && then != null) {
            then.accept(update, null);

        } else {
            super.skip(update);
            ask(update);
        }
    }

    public void cancel(Update update) {
        if (onCancel != null) {
            onCancel.accept(update);

        } else {
            // todo: enhance this design
            throw new UnsupportedOperationException("Cancel handler must be not null");
        }
    }

    public AdminBotDialogState<R> ask(Update update) {
        ask(update, false);
        return this;
    }

    public AdminBotDialogState<R> ask(Update update, boolean removeKeyboard) {
        ask(update, null, removeKeyboard);
        return this;
    }

    public AdminBotDialogState<R> ask(Update update, String message) {
        ask(update, message, false);
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

    public AdminBotDialogState<R> setSkippable(boolean skippable) {
        this.skippable = skippable;
        return this;
    }
}

