package com.sushnaya.telegrambot.state.user;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.*;
import com.sushnaya.telegrambot.command.user.*;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class UnregisteredState extends DefaultState {
    public static final ReplyKeyboardMarkup KEYBOARD_MARKUP = new ReplyKeyboardMarkup();
    
    static {
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton signupButton = new KeyboardButton("Отправить мой номер телефона");
        signupButton.setRequestContact(true);
        row.add(signupButton);
        keyboard.add(row);
        KEYBOARD_MARKUP.setKeyboard(keyboard);
        KEYBOARD_MARKUP.setResizeKeyboard(true);
        KEYBOARD_MARKUP.setOneTimeKeyboad(true);
    }

    private final Command contactCommand = new SaveContact(bot);

    public UnregisteredState(SushnayaBot bot) {
        super(bot);
    }

    protected Start createStartCommand() {
        return new UnregisteredStart(bot);
    }

    protected UnknownCommand createUnknownCommand() {
        return new UnregisteredUnknownCommand(bot);
    }

    protected Command getCommand(Update update) {
        if (update.hasMessage() && update.getMessage().getContact() != null) {
            return contactCommand;
        }

        return super.getCommand(update);
    }
}
