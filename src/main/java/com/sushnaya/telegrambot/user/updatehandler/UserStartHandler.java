package com.sushnaya.telegrambot.user.updatehandler;

import com.sushnaya.telegrambot.SushnayaBot;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;

public class UserStartHandler extends UserMenuHandler {

    public UserStartHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        bot.say(update, MESSAGES.greeting(), true);

        super.handle(update);
    }
}
