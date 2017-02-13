package com.sushnaya.telegrambot.command.admin;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.user.Start;
import com.sushnaya.telegrambot.state.admin.AdminOnDefaultState;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

public class AdminStart extends Start {

    public AdminStart(SushnayaBot bot) {
        super(bot);
    }

    protected SendMessage createSendMessage(Update update) {
        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setReplyMarkup(AdminOnDefaultState.KEYBOARD_MARKUP)
                .setParseMode(ParseMode.MARKDOWN)
                .setText("*Что может этот бот?*\n\n" +
                        "SushnayaBot предоставляет возможность редактировать меню, " +
                        "совершать массовые оповещения " +
                        "и просматривать статистику доставок сети \"Сушная\". \uD83D\uDC47");
    }
}

