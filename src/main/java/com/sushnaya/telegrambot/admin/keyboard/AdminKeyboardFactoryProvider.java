package com.sushnaya.telegrambot.admin.keyboard;

import com.sushnaya.telegrambot.SushnayaBot;

public class AdminKeyboardFactoryProvider {
    public static final AdminKeyboardMarkupFactory STARTUP_MARKUP_FACTORY =
            new StartupAdminKeyboardMarkupFactory();
    public static final AdminKeyboardMarkupFactory DEFAULT_MARKUP_FACTORY =
            new DefaultAdminKeyboardMarkupFactory();
    public static final AdminKeyboardMarkupFactory NO_PUBLISHED_PRODUCTS_MARKUP_FACTORY =
            new NoPublishedProductsAdminKeyboardMarkupFactory();

    public static AdminKeyboardMarkupFactory getKeyboardFactory(SushnayaBot bot) {
        return !bot.hasProducts() ? STARTUP_MARKUP_FACTORY :
                !bot.hasPublishedProducts() ? NO_PUBLISHED_PRODUCTS_MARKUP_FACTORY :
                        DEFAULT_MARKUP_FACTORY;
    }

    private AdminKeyboardFactoryProvider() {
    }
}
