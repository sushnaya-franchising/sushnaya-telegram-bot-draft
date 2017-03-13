package com.sushnaya.telegrambot.admin.state;

import com.sushnaya.telegrambot.BotState;
import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.UpdateHandler;
import com.sushnaya.telegrambot.admin.updatehandler.*;
import com.sushnaya.telegrambot.user.updatehandler.UserNextProductInCategoryHandler;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.REPLY_KEYBOARD_REMOVE;

public abstract class AdminBotDialogState<R> extends BotState {
    public static final Function<Update, Command> SKIP_COMMAND_PARSER = u -> {
        if (u.hasMessage() && u.getMessage().hasText()) {
            final String text = u.getMessage().getText();
            if (text.equalsIgnoreCase(MESSAGES.skip())) {
                return SKIP;
            }
        }
        return NOP;
    };
    private static final Consumer<Update> NULL_CANCEL = u -> {
        throw new UnsupportedOperationException();
    };

    private Consumer<Update> onCancel = NULL_CANCEL;
    private Function<Update, Command> extraCommandParser;
    private BiConsumer<Update, R> then;
    private String defaultMessage;
    private ReplyKeyboard defaultKeyboard;
    private String helpMessage;
    private boolean skippable;

    public AdminBotDialogState(SushnayaBot bot) {
        super(bot);

        registerUpdateHandler(START, cancelBefore(new AdminStartHandler(bot)));
        registerUpdateHandler(MENU, cancelBefore(new AdminMenuHandler(bot)));
        registerUpdateHandler(NEXT_PRODUCT_IN_CATEGORY, cancelBefore(new UserNextProductInCategoryHandler(bot)));
        registerUpdateHandler(ADMIN_DASHBOARD, cancelBefore(new DashboardHandler(bot)));
        registerUpdateHandler(CREATE_MENU, cancelBefore(new CreateMenuHandler(bot)));
        registerUpdateHandler(CREATE_CATEGORY, cancelBefore(new CreateCategoryHandler(bot)));
        registerUpdateHandler(CREATE_PRODUCT, cancelBefore(new CreateProductHandler(bot)));
        registerUpdateHandler(EDIT_MENU, cancelBefore(new EditMenuHandler(bot)));
        registerUpdateHandler(EDIT_CATEGORY, cancelBefore(new EditCategoryHandler(bot)));
        registerUpdateHandler(EDIT_PRODUCT, cancelBefore(new EditProductHandler(bot)));
        registerUpdateHandler(DELETE_MENU, cancelBefore(new DeleteMenuHandler(bot)));
        registerUpdateHandler(DELETE_CATEGORY, cancelBefore(new DeleteCategoryHandler(bot)));
        registerUpdateHandler(DELETE_PRODUCT, cancelBefore(new DeleteProductHandler(bot)));
        registerUpdateHandler(CANCEL, this::cancel);
        registerUpdateHandler(SKIP, this::skip);

        final UpdateHandler originalHelpHandler = new AdminHelpHandler(bot);
        registerUpdateHandler(HELP, u -> {
            if (helpMessage != null) {
                bot.say(u, helpMessage);
            } else {
                originalHelpHandler.handle(u);
            }
            ask(u);
        });
    }

    public AdminBotDialogState<R> setExtraCommandParser(Function<Update, Command> extraCommandParser) {
        this.extraCommandParser = extraCommandParser;
        return this;
    }

    @Override
    protected Command parseCommand(Update update) {
        Command result = SKIP_COMMAND_PARSER.apply(update);

        if (result == null || result == NOP) {
            result = extraCommandParser != null ? extraCommandParser.apply(update) : NOP;
        }

        return result == null || result == NOP ? super.parseCommand(update) : result;
    }

    public AdminBotDialogState<R> onCancel(Consumer<Update> onCancel) {
        this.onCancel = onCancel == null ? NULL_CANCEL : onCancel;
        return this;
    }

    private UpdateHandler cancelBefore(UpdateHandler handler) {
        return u -> {
            cancel(u);
            handler.handle(u);
        };
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

    public AdminBotDialogState<R> ifThen(Command command, UpdateHandler handler) {
        registerUpdateHandler(command, handler);
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
    protected UpdateHandler getUpdateHandler(Command command) {
        UpdateHandler handler = super.getUpdateHandler(command);

        if (handler == null) {
            handler = u -> handleUpdate(u, then, this::ask);
        }

        return handler;
    }

    protected abstract void handleUpdate(Update update, BiConsumer<Update, R> ok,
                                         BiConsumer<Update, String> ko);

    public void skip(Update update) {
        if (skippable && then != null) {
            then.accept(update, null);

        } else {
            bot.say(update, MESSAGES.nothingToSkip());
            ask(update);
        }
    }

    public void cancel(Update update) {
        onCancel.accept(update);
    }

    public AdminBotDialogState<R> ask(Update update) {
        ask(update, false);
        return this;
    }

    public AdminBotDialogState<R> ask(Update update, ReplyKeyboard keyboard) {
        ask(update, null, keyboard);
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
        say(update, message, keyboard);
        bot.setState(update, this);

        return this;
    }

    private String prepareMessage(String message) {
        return (message == null ? defaultMessage : message) +
                (helpMessage == null ? "" : ' ' + HELP.getUri());
    }

    public void say(Update update, String message, ReplyKeyboard keyboard) {
        if (message == null && defaultMessage == null) throw new IllegalStateException(
                "Default message must be not null");

        if (defaultMessage == null) setDefaultMessage(message);
        if (defaultKeyboard == null) setDefaultKeyboard(keyboard);

        bot.say(update, prepareMessage(message),
                keyboard == null ? defaultKeyboard : keyboard);
    }

    public AdminBotDialogState<R> setSkippable(boolean skippable) {
        this.skippable = skippable;
        return this;
    }
}

