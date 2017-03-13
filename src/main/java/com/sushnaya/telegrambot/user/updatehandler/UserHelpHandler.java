package com.sushnaya.telegrambot.user.updatehandler;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;

public class UserHelpHandler extends SushnayaBotUpdateHandler {
    public UserHelpHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        // todo: add change city command if more than 1 menu
        bot.say(update, MESSAGES.userHelp(), true);
    }
}
