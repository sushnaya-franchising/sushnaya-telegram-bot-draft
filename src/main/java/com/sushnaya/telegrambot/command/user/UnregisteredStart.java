package com.sushnaya.telegrambot.command.user;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.state.user.UnregisteredState;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

public class UnregisteredStart extends Start {
    public UnregisteredStart(SushnayaBot bot) {
        super(bot);
    }

    protected SendMessage createSendMessage(Update update) {
        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN)
                .setReplyMarkup(UnregisteredState.KEYBOARD_MARKUP)
                .setText("*Что может этот бот?*\n\n" +
                        "SushnayaBot покажет перечень блюд и напитков сети \"Сушная\" " +
                        "в вашем городе.\n\n" +
                        "Для того, чтобы я вас зарегистрировал, " +
                        "предоставьте _ваш номер телефона_.");
    }
}
