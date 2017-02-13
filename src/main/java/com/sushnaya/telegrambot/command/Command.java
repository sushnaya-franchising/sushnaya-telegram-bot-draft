package com.sushnaya.telegrambot.command;

import com.sushnaya.telegrambot.SushnayaBot;
import org.telegram.telegrambots.api.objects.Update;

public abstract class Command {
    protected final SushnayaBot bot;

    public Command(SushnayaBot bot) {
        this.bot = bot;
    }

    public abstract void execute(Update update);

    public static class Uri {
        public static final String START_URI = "/start";
        public static final String MENU_URI = "/menu";
        public static final String SEARCH_URI = "/search";
        public static final String HELP_URI = "/help";
        public static final String MANAGEMENT_URI = "/manage";
        public static final String BACK_URI = "/back";
        public static final String CANCEL_URI = "/cancel";

        public static final String NEW_MENU_URI = "/newmenu";
        public static final String NEW_MENU_CATEGORY_URI = "/newmenucategory";
        public static final String NEW_PRODUCT_URI = "/newproduct";
        public static final String NEW_DELIVERY_ZONE = "/newdeliveryzone";
        public static final String NEW_USER_URI = "/newuser";

        public static final String BRANDS_MANAGEMENT_URI = "/managebrands";
        public static final String NEW_BRAND_URI = "/newbrand";
        public static final String EDIT_BRAND_URI = "/editbrand_";
        public static final String EDIT_BRAND_NAME_URI = "/editbrandname_";
        public static final String DELETE_BRAND_URI = "/delbrand_";

        public static final String MENU_MANAGEMENT_URI = "/managemenu";
        public static final String EDIT_MENU_URI = "/editmenu_";

        public static final String DELIVERY_ZONES_MANAGEMENT_URI = "/managedeliveryzones";
        public static final String EDIT_DELIVERY_ZONE_URI = "/editdeliveryzone_";

        public static final String USERS_MANAGEMENT_URI = "/manageusers";
        public static final String EDIT_USER_URI = "/edituser_";

        public static final String NOP_URI = "/nop";
        public static final String ADMIN_OFF_URI = "/nimda";
        public static final String ADMIN_ON_URI = "/admin";

        public static String parseCommandUri(String s) {
            if (s.equalsIgnoreCase(START_URI)) {
                return START_URI;
            }

            if (s.equalsIgnoreCase(HELP_URI)) {
                return HELP_URI;
            }

            if (s.equalsIgnoreCase(MANAGEMENT_URI) ||
                    s.equalsIgnoreCase(ButtonLabel.MANAGEMENT_BUTTON_LABEL) ||
                    s.equalsIgnoreCase(ButtonEmojiLabel.MANAGEMENT_BUTTON_LABEL)) {
                return MANAGEMENT_URI;
            }

            if (s.equalsIgnoreCase(BRANDS_MANAGEMENT_URI) ||
                    s.equalsIgnoreCase(ButtonLabel.BRANDS_MANAGEMENT_BUTTON_LABEL) ||
                    s.equalsIgnoreCase(ButtonEmojiLabel.BRANDS_MANAGEMENT_BUTTON_LABEL)) {
                return BRANDS_MANAGEMENT_URI;
            }

            if (s.equalsIgnoreCase(BACK_URI) ||
                    s.equalsIgnoreCase(ButtonLabel.BACK_BUTTON_LABEL) ||
                    s.equalsIgnoreCase(ButtonEmojiLabel.BACK_BUTTON_LABEL)) {
                return BACK_URI;
            }

            if (s.equalsIgnoreCase(CANCEL_URI) ||
                    s.equalsIgnoreCase(ButtonLabel.CANCEL_BUTTON_LABEL) ||
                    s.equalsIgnoreCase(ButtonEmojiLabel.CANCEL_BUTTON_LABEL)) {
                return BACK_URI;
            }

            if (s.equalsIgnoreCase(NEW_BRAND_URI) ||
                    s.equalsIgnoreCase(ButtonLabel.NEW_BRAND_BUTTON_LABEL) ||
                    s.equalsIgnoreCase(ButtonEmojiLabel.NEW_BRAND_BUTTON_LABEL)) {
                return NEW_BRAND_URI;
            }

            if (s.startsWith(EDIT_BRAND_URI)) {
                return EDIT_BRAND_URI;
            }

            return NOP_URI;
        }
    }

    public static class ButtonEmojiLabel {
        public static final String MANAGEMENT_BUTTON_LABEL = "\uD83D\uDD27 " + ButtonLabel.MANAGEMENT_BUTTON_LABEL;
        public static final String NOTIFICATION_BUTTON_LABEL = "\uD83D\uDCE3 " + ButtonLabel.NOTIFICATION_BUTTON_LABEL;
        public static final String STATISTICS_BUTTON_LABEL = "\uD83D\uDCCA " + ButtonLabel.STATISTICS_BUTTON_LABEL;
        public static final String MENU_BUTTON_LABEL = "" + ButtonLabel.MENU_BUTTON_LABEL;
        public static final String SEARCH_BUTTON_LABEL = "\uD83D\uDD0D " + ButtonLabel.SEARCH_BUTTON_LABEL;

        public static final String BRANDS_MANAGEMENT_BUTTON_LABEL = "\uD83C\uDFF7 " + ButtonLabel.BRANDS_MANAGEMENT_BUTTON_LABEL;
        public static final String MENU_MANAGEMENT_BUTTON_LABEL = "\uD83C\uDF54 " + ButtonLabel.MENU_MANAGEMENT_BUTTON_LABEL;
        public static final String DELIVERY_MANAGEMENT_BUTTON_LABEL = "\uD83D\uDCE6 " + ButtonLabel.DELIVERY_MANAGEMENT_BUTTON_LABEL;
        public static final String USERS_MANAGEMENT_BUTTON_LABEL = "\uD83D\uDC64 " + ButtonLabel.USERS_MANAGEMENT_BUTTON_LABEL;

        public static final String NEW_BRAND_BUTTON_LABEL = "\uD83C\uDFF7 " + ButtonLabel.NEW_BRAND_BUTTON_LABEL;

        public static final String BACK_BUTTON_LABEL = "" + ButtonLabel.BACK_BUTTON_LABEL;
        public static final String CANCEL_BUTTON_LABEL = "" + ButtonLabel.CANCEL_BUTTON_LABEL;

        public static final String ADMIN_ON_BUTTON_LABEL = "" + ButtonLabel.ADMIN_ON_BUTTON_LABEL;
        public static final String ADMIN_OFF_BUTTON_LABEL = "" + ButtonLabel.ADMIN_OFF_BUTTON_LABEL;
    }

    public static class ButtonLabel {
        public static final String MANAGEMENT_BUTTON_LABEL = "Управление";
        public static final String NOTIFICATION_BUTTON_LABEL = "Оповещение";
        public static final String STATISTICS_BUTTON_LABEL = "Статистика";
        public static final String MENU_BUTTON_LABEL = "Меню";
        public static final String SEARCH_BUTTON_LABEL = "Поиск";

        public static final String BRANDS_MANAGEMENT_BUTTON_LABEL = "Брэнды";
        public static final String MENU_MANAGEMENT_BUTTON_LABEL = "Меню";
        public static final String DELIVERY_MANAGEMENT_BUTTON_LABEL = "Зоны доставки";
        public static final String USERS_MANAGEMENT_BUTTON_LABEL = "Пользователи";

        public static final String NEW_BRAND_BUTTON_LABEL = "Новый брэнд";

        public static final String BACK_BUTTON_LABEL = "Назад";
        public static final String CANCEL_BUTTON_LABEL = "Отмена";
        
        public static final String ADMIN_ON_BUTTON_LABEL = "Вкл режим администрирования";
        public static final String ADMIN_OFF_BUTTON_LABEL = "Выкл режим администрирования";
    }
}
