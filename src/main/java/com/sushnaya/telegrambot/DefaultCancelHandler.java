package com.sushnaya.telegrambot;

import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;

public class DefaultCancelHandler extends SushnayaBotUpdateHandler {
    public DefaultCancelHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        bot.say(update, MESSAGES.nothingToCancel());
    }
}
