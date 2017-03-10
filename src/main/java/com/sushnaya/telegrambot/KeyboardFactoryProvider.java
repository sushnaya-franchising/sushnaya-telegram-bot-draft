package com.sushnaya.telegrambot;

import com.sushnaya.entity.User;
import com.sushnaya.telegrambot.admin.keyboard.AdminKeyboardFactoryProvider;
import com.sushnaya.telegrambot.admin.keyboard.AdminKeyboardMarkupFactory;
import com.sushnaya.telegrambot.user.keyboard.UserKeyboardMarkupFactory;

public class KeyboardFactoryProvider {
    private static final UserKeyboardMarkupFactory USER_KEYBOARD_MARKUP_FACTORY =
            new UserKeyboardMarkupFactory();

    public static KeyboardMarkupFactory getKeyboardFactory(User user, SushnayaBot bot) {
        return user.isAdmin() ? AdminKeyboardFactoryProvider.getKeyboardFactory(bot) :
                USER_KEYBOARD_MARKUP_FACTORY;
    }

    public static AdminKeyboardMarkupFactory getAdminKeyboardFactory(SushnayaBot bot) {
        return AdminKeyboardFactoryProvider.getKeyboardFactory(bot);
    }

    public static UserKeyboardMarkupFactory getUserKeyboardFactory() {
        return USER_KEYBOARD_MARKUP_FACTORY;
    }

    private KeyboardFactoryProvider() {
    }
}
