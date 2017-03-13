package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import org.telegram.telegrambots.api.objects.Update;

public class RestoreCategoryHandler extends SushnayaBotUpdateHandler {
    public RestoreCategoryHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
