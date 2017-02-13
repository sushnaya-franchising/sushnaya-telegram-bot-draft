package com.sushnaya.telegrambot.command.user;

import com.sushnaya.telegrambot.SushnayaBot;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class UnregisteredUnknownCommand extends UnknownCommand {
    public UnregisteredUnknownCommand(SushnayaBot bot) {
        super(bot);
    }

    protected SendMessage createSendMessage(Update update) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton signupButton = new KeyboardButton("Отправить мой номер телефона");
        signupButton.setRequestContact(true);
        row.add(signupButton);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);

        return super.createSendMessage(update)
                .setReplyMarkup(keyboardMarkup)
                .setText("Кажется, вы не зарегистрированы. " +
                        "Предоставьте _ваш номер телефона_ для того, чтобы я смог " +
                        "ваc зарегистрировать.");
    }
}
