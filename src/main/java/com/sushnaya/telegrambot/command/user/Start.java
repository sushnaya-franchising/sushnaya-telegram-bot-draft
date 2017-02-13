package com.sushnaya.telegrambot.command.user;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.ReplyCommand;
import com.sushnaya.telegrambot.state.user.DefaultState;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

public class Start extends ReplyCommand {
    public Start(SushnayaBot bot) {
        super(bot);
    }

    protected SendMessage createSendMessage(Update update) {
        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setReplyMarkup(DefaultState.KEYBOARD_MARKUP)
                .setParseMode(ParseMode.MARKDOWN)
                .setText("*Что может этот бот?*\n\n" +
                        "SushnayaBot покажет перечень блюд и напитков сети \"Сушная\" " +
                        "в вашем городе. \uD83D\uDC47");
    }
}

