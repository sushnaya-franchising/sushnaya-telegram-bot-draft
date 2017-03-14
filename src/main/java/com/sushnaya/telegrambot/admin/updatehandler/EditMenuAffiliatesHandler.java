package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import org.telegram.telegrambots.api.objects.Update;

public class EditMenuAffiliatesHandler extends SushnayaBotUpdateHandler {
    public EditMenuAffiliatesHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        bot.handleUnknownCommand(update);
    }
}
