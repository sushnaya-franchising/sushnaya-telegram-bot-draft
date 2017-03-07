package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.user.updatehandler.UserMenuHandler;
import org.telegram.telegrambots.api.objects.Update;

public class AdminMenuHandler extends UserMenuHandler{
    public AdminMenuHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        if (!bot.hasPublishedProducts()) {
            bot.handleCommand(update, Command.ADMIN_DASHBOARD);

        } else {
            super.handle(update);
        }
    }
}
