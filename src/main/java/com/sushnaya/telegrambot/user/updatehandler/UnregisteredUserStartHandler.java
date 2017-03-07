package com.sushnaya.telegrambot.user.updatehandler;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.singleRequestContactButtonKeyboard;

public class UnregisteredUserStartHandler extends SushnayaBotUpdateHandler {
    public UnregisteredUserStartHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        bot.say(update, getGreetingMessageText(),
                singleRequestContactButtonKeyboard(SushnayaBot.MESSAGES.sendContact()));
    }

    private static String getGreetingMessageText() {
        return SushnayaBot.MESSAGES.greeting() + "\n\n" +
                SushnayaBot.MESSAGES.unregisteredUserContactInquiry();
    }
}
