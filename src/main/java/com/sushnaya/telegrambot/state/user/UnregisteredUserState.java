package com.sushnaya.telegrambot.state.user;

import com.sushnaya.entity.User;
import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.UnregisteredKeyboardMarkupFactory;
import org.telegram.telegrambots.api.objects.Contact;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.Command.SEND_CONTACT;
import static com.sushnaya.telegrambot.util.UpdateUtil.getTelegramUserId;

public class UnregisteredUserState extends UserDefaultState {
    private static final UnregisteredKeyboardMarkupFactory KEYBOARD_MARKUP_FACTORY =
            new UnregisteredKeyboardMarkupFactory();

    public UnregisteredUserState(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void start(Update update) {
        bot.say(update, getGreetingMessageText(), KEYBOARD_MARKUP_FACTORY.contactRequestMarkup());
    }

    @Override
    protected String getGreetingMessageText() {
        return super.getGreetingMessageText() + "\n\n" +
                MESSAGES.unregisteredUserContactInquiry();
    }

    public void menu(Update update) {
        bot.say(update, MESSAGES.askToRegister(),
                KEYBOARD_MARKUP_FACTORY.contactRequestMarkup());
    }

    public UnregisteredKeyboardMarkupFactory getKeyboardMarkupFactory() {
        return KEYBOARD_MARKUP_FACTORY;
    }

    @Override
    public void help(Update update) {
        bot.say(update, MESSAGES.unregisteredUserHelp(),
                KEYBOARD_MARKUP_FACTORY.contactRequestMarkup());
    }

    @Override
    public boolean handle(Update update) {
        if (super.handle(update)) return true;

        if (Command.parseCommand(update) == SEND_CONTACT) {
            registerUser(update);
            bot.say(update, MESSAGES.registrationSuccessful());
            bot.setUserDefaultState(getTelegramUserId(update)).menu(update);

            return true;
        }

        return false;
    }

    private User registerUser(Update update) {
        User user = User.fromTelegramUser(update.getMessage().getFrom());
        Contact contact = update.getMessage().getContact();
        user.setPhoneNumber(contact.getPhoneNumber());

        bot.registerUser(user);

        return user;
    }
}
