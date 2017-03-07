package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;

public class AdminHelpHandler extends SushnayaBotUpdateHandler {
    public AdminHelpHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        bot.say(update, MESSAGES.userHelp(MENU, HELP, CANCEL) + "\n\n" +
                MESSAGES.administrationAllowed() + "\n\n" +
                MESSAGES.adminHelp(ADMIN_DASHBOARD, CREATE_PRODUCT,
                        CREATE_CATEGORY, CREATE_MENU), true);
    }
}
