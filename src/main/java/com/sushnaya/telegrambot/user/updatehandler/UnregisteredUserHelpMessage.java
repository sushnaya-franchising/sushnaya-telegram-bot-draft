package com.sushnaya.telegrambot.user.updatehandler;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.singleRequestContactButtonKeyboard;

public class UnregisteredUserHelpMessage extends SushnayaBotUpdateHandler {
    public UnregisteredUserHelpMessage(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        bot.say(update, SushnayaBot.MESSAGES.unregisteredUserHelp(),
                singleRequestContactButtonKeyboard(SushnayaBot.MESSAGES.sendContact()));
    }
}
