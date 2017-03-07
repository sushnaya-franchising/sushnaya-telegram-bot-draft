package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.telegrambot.SushnayaBot;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;

public class AdminStartHandler extends AdminMenuHandler {
    public AdminStartHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        bot.say(update, getGreetingMessage(), true);

        super.handle(update);
    }

    public String getGreetingMessage() {
        return MESSAGES.greeting() + "\n\n" + MESSAGES.administrationAllowed();
    }
}
