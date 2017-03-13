package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;

public class AdminHelpHandler extends SushnayaBotUpdateHandler {
    public AdminHelpHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        bot.say(update, MESSAGES.userHelp() + "\n\n" +
                MESSAGES.administrationAllowed() + "\n\n" +
                MESSAGES.adminHelp(), true);
    }
}
