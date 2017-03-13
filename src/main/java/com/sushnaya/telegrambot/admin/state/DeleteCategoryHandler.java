package com.sushnaya.telegrambot.admin.state;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import com.sushnaya.telegrambot.UpdateHandler;
import org.telegram.telegrambots.api.objects.Update;

public class DeleteCategoryHandler extends SushnayaBotUpdateHandler {
    public DeleteCategoryHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
