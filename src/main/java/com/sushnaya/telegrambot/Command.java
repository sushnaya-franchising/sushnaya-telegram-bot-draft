package com.sushnaya.telegrambot;

import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.Messages.getDefaultMessages;

public enum Command {
    NOP("/nop"),
    SEND_CONTACT("/sendcontact", getDefaultMessages().sendContact()),
    START("/start"),
    HELP("/help"),
    HOME("/home"),
    MENU("/menu", getDefaultMessages().menu()),
    SEARCH("/search", getDefaultMessages().search()),
    ADMIN_DASHBOARD("/admin", getDefaultMessages().dashboard()),
    CANCEL("/cancel", getDefaultMessages().cancel()),
    SKIP("/skip", getDefaultMessages().skip()),
    CONTINUE("/continue", getDefaultMessages().continueMessage()),
    BACK_TO_HOME("/backtohome", getDefaultMessages().backToHome()),
    BACK_TO_DASHBOARD("/backtodashboard", getDefaultMessages().backToDashboard()),
    SETTINGS("/settings", getDefaultMessages().settings()),
    STATISTICS("/stat", getDefaultMessages().statistics()),
    NOTIFY("/notify", getDefaultMessages().notify_()),
    PROMOTIONS("/promotions", getDefaultMessages().promotions()),
    CREATE_MENU("/newmenu", getDefaultMessages().createMenu()),
    EDIT_MENU("/editmenu", getDefaultMessages().editMenu()),
    EDIT_LOCALITY("/editlocality", getDefaultMessages().editLocality()),
    DELETE_MENU("/deletemenu", getDefaultMessages().deleteMenu()),
    MENU_CATEGORIES("/categories"),
    EDIT_MENU_CATEGORIES("/editcategories", getDefaultMessages().editCategories()),
    CREATE_CATEGORY("/newcategory", getDefaultMessages().createCategory()),
    DELETE_CATEGORY("/deletecategory", getDefaultMessages().deleteCategory()),
    EDIT_CATEGORY("/editcategory"),
    PRODUCTS("/products"),
    EDIT_PRODUCTS("/editproducts", getDefaultMessages().editProducts()),
    SET_PRODUCT_SUBHEADING("/setproductsubheading", getDefaultMessages().setProductSubheading()),
    SET_PRODUCT_DESCRIPTION("/setproductdescription", getDefaultMessages().setProductDescription()),
    PUBLISH_PRODUCT("/publishproduct", getDefaultMessages().publishProduct()),
    SKIP_PRODUCT_PUBLICATION(SKIP.getUri(), getDefaultMessages().skipProductPublication()),
    CREATE_PRODUCT_IN_MENU("/newproductinmenu", getDefaultMessages().createProductInMenu()),
    CREATE_PRODUCT_IN_CATEGORY("/newproductincategory", getDefaultMessages().createProductInCurrentCategory());

    private String uri;
    private String text;

    Command(String uri) {
        this(uri, null);
    }

    Command(String uri, String text) {
        this.uri = uri;
        this.text = text;
    }

    public String getUri() {
        return uri;
    }

    public String getUriForId(int id) {
        return getUri() + '_' + id;
    }

    public String getText() {
        return text;
    }

    public static Command parse(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return parse(update.getMessage().getText());
        }

        if (update.hasCallbackQuery()) {
            return parse(update.getCallbackQuery().getData());
        }

        if (update.hasMessage() && update.getMessage().getContact() != null) {
            return SEND_CONTACT;
        }

        return NOP;
    }

    public static Command parse(String input) {
        String commandText = decodeCommandText(input);

        for (Command command : values()) {
            if (commandText.equalsIgnoreCase(command.getText()) ||
                    commandText.equals(command.getUri())) {
                return command;
            }
        }

        return NOP;
    }

    public static String decodeCommandText(String v) {
        if (v == null) return null;
        int i = v.lastIndexOf('_');
        if (i < 0) return v;
        return i > 0 ? v.substring(0, i) : "";
    }

    public static Integer decodeId(String v) {
        if (v == null) return null;
        int i = v.lastIndexOf('_');
        try {
            return i > 0 ? Integer.parseInt(
                    v.substring(i + 1, v.length())) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
