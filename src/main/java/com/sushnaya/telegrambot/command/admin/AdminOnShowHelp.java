package com.sushnaya.telegrambot.command.admin;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.user.ShowHelp;
import com.sushnaya.telegrambot.state.admin.AdminOnDefaultState;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.command.Command.Uri.*;

public class AdminOnShowHelp extends ShowHelp {
    public AdminOnShowHelp(SushnayaBot bot) {
        super(bot);
    }

    protected SendMessage createSendMessage(Update update) {
        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setReplyMarkup(AdminOnDefaultState.KEYBOARD_MARKUP)
                .setParseMode(ParseMode.MARKDOWN)
                .setText("*Режим администратора включен*\n\n" +
                        "*Меню* - " + MENU_URI + "\n\n" +
                        "*Поиск* - " + SEARCH_URI + "\n\n" +
                        "*Управление* - " + MANAGEMENT_URI + "\n\n" +
                        "*Выкл режим администратора* - " + ADMIN_OFF_URI);
    }
}
