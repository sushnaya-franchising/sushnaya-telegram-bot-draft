package com.sushnaya.telegrambot.command.admin;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.state.admin.AdminOnDefaultState;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

public class RevealDefault extends Back {

    public RevealDefault(SushnayaBot bot) {
        super(bot);
    }

    protected SendMessage createSendMessage(Update update) {
        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setReplyMarkup(AdminOnDefaultState.KEYBOARD_MARKUP)
                .setParseMode(ParseMode.MARKDOWN)
                .setText("Хотите отредактировать меню, " +
                        "провести массовое оповещение клиентов " +
                        "или исследовать статистику доставок? \uD83D\uDC47");
    }
}
