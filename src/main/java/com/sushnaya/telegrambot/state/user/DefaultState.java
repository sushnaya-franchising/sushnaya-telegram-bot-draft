package com.sushnaya.telegrambot.state.user;

import com.google.common.collect.Lists;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.Command;
import com.sushnaya.telegrambot.command.user.ShowHelp;
import com.sushnaya.telegrambot.command.user.Start;
import com.sushnaya.telegrambot.command.user.UnknownCommand;
import com.sushnaya.telegrambot.state.SushnayaBotState;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.sushnaya.telegrambot.command.Command.ButtonEmojiLabel.MENU_BUTTON_LABEL;
import static com.sushnaya.telegrambot.command.Command.ButtonEmojiLabel.SEARCH_BUTTON_LABEL;
import static com.sushnaya.telegrambot.command.Command.Uri.*;

public class DefaultState extends SushnayaBotState {

    public static final InlineKeyboardMarkup KEYBOARD_MARKUP = new InlineKeyboardMarkup();

    static {
        List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        List<InlineKeyboardButton> row = Lists.newArrayList(
                new InlineKeyboardButton().setText(MENU_BUTTON_LABEL).setCallbackData(MENU_URI),
                new InlineKeyboardButton().setText(SEARCH_BUTTON_LABEL).setSwitchInlineQueryCurrentChat("")
        );
        keyboard.add(row);
        KEYBOARD_MARKUP.setKeyboard(keyboard);
    }

    private final Command startCommand = createStartCommand();
    private final Command helpCommand = createShowHelpCommand();
    private final Command unknownCommand = createUnknownCommand();

    public DefaultState(SushnayaBot bot) {
        super(bot);
    }

    protected ShowHelp createShowHelpCommand() {
        return new ShowHelp(bot);
    }

    protected Start createStartCommand() {
        return new Start(bot);
    }

    protected UnknownCommand createUnknownCommand() {
        return new UnknownCommand(bot);
    }

    protected Command getCommand(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (parseCommandUri(update.getMessage().getText())) {
                case START_URI:
                    return startCommand;

                case HELP_URI:
                    return helpCommand;
            }
        }

        return unknownCommand;
    }

    public void process(Update update) {
        getCommand(update).execute(update);
    }
}
