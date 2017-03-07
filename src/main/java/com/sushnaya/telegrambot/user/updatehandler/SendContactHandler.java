package com.sushnaya.telegrambot.user.updatehandler;

import com.sushnaya.entity.User;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import org.telegram.telegrambots.api.objects.Contact;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;

public class SendContactHandler extends SushnayaBotUpdateHandler {
        public SendContactHandler(SushnayaBot bot) {
            super(bot);
        }

        @Override
        public void handle(Update update) {
            registerUser(update);

            bot.say(update, MESSAGES.registrationSuccessful());

            bot.setUserDefaultState(update).revealMenu(update);
        }

        private void registerUser(Update update) {
            User user = User.fromTelegramUser(update.getMessage().getFrom());
            Contact contact = update.getMessage().getContact();

            user.setPhoneNumber(contact.getPhoneNumber());

            bot.getDataStorage().saveUser(user);
        }
    }