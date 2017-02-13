package com.sushnaya.telegrambot.command.admin;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.user.ShowHelp;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.command.Command.Uri.*;

public class AdminOffShowHelp extends ShowHelp {
    public AdminOffShowHelp(SushnayaBot bot) {
        super(bot);
    }

    protected SendMessage createSendMessage(Update update) {
        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN)
                .setText("*Режим администратора выключен*\n\n" +
                        "*Меню* - " + MENU_URI + "\n\n" +
                        "*Поиск* - " + SEARCH_URI + "\n\n" +
                        "*Вкл режим администратора* - " + ADMIN_ON_URI);
    }
}
