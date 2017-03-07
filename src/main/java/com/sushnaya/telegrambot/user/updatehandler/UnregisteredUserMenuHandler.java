package com.sushnaya.telegrambot.user.updatehandler;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.singleRequestContactButtonKeyboard;

public class UnregisteredUserMenuHandler extends SushnayaBotUpdateHandler {
    public UnregisteredUserMenuHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        bot.say(update, MESSAGES.askToRegister(),
                singleRequestContactButtonKeyboard(MESSAGES.sendContact()));
    }
}
