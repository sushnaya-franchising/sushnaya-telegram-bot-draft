package com.sushnaya.telegrambot;

import org.telegram.telegrambots.api.objects.Update;

import java.util.function.Function;

import static com.sushnaya.telegrambot.Messages.getDefaultMessages;

// todo: refactor command design (do not use enum)
public enum Command {
    NOP("/nop"),
    SEND_CONTACT("/sendcontact", getDefaultMessages().sendContact()),
    START("/start"),
    HELP("/help"),
    MENU("/menu", getDefaultMessages().menu()),
    SEARCH("/search", getDefaultMessages().search()),
    ADMIN_DASHBOARD("/admin", getDefaultMessages().dashboard()),
    CANCEL("/cancel", getDefaultMessages().cancel()),
    SKIP("/skip", getDefaultMessages().skip()),
    CONTINUE("/continue", getDefaultMessages().continueMessage()),
    BACK_TO_MENU("/backtomenu", getDefaultMessages().backToMenu()),
    BACK_TO_DASHBOARD("/backtodashboard", getDefaultMessages().backToDashboard()),
    CLOSE_DASHBOARD("/closedashboard", getDefaultMessages().closeDashboard()),
    SETTINGS("/settings", getDefaultMessages().settings()),
    STATISTICS("/stat", getDefaultMessages().statistics()),
    NOTIFY("/notify", getDefaultMessages().notify_()),
    PROMOTIONS("/promotions", getDefaultMessages().promotions()),
    CREATE_MENU("/newmenu", getDefaultMessages().createMenu()),
    EDIT_MENU("/editmenu", getDefaultMessages().editMenu()),
    EDIT_LOCALITY("/editlocality", getDefaultMessages().editLocality()),
    DELETE_MENU("/deletemenu", getDefaultMessages().deleteMenu()),
    CATEGORY("/category"),
    EDIT_CATEGORY("/editcategories", getDefaultMessages().editCategories()),
    CREATE_CATEGORY("/newcategory", getDefaultMessages().createCategory()),
    DELETE_CATEGORY("/deletecategory", getDefaultMessages().deleteCategory()),
    PRODUCTS("/products"),
    EDIT_PRODUCTS("/editproducts", getDefaultMessages().editProducts()),
    SET_PRODUCT_SUBHEADING("/setproductsubheading", getDefaultMessages().setProductSubheading()),
    SET_PRODUCT_DESCRIPTION("/setproductdescription", getDefaultMessages().setProductDescription()),
    PUBLISH_PRODUCT("/publishproduct", getDefaultMessages().publishProduct()),
    SKIP_PRODUCT_PUBLICATION(SKIP.getUri(), getDefaultMessages().skipProductPublication()),
    CREATE_PRODUCT("/newproduct"),
    CREATE_PRODUCT_IN_MENU("/newproductinmenu", getDefaultMessages().createProductInMenu()),
    CREATE_PRODUCT_IN_CATEGORY("/newproductincategory", getDefaultMessages()::createProductInCategory);

    private Function<String, String> textSupplier;
    private String uri;
    private String text;

    Command(String uri) {
        this.uri = uri;
    }

    Command(String uri, String text) {
        this.uri = uri;
        this.text = text;
    }

    Command(String uri, Function<String, String> textSupplier) {
        this.uri = uri;
        this.textSupplier = textSupplier;
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

    public String getText(String arg) {
        return textSupplier.apply(arg);
    }

    public static Command parseCommand(Update update) {
        if (update.hasMessage() && update.getMessage().getContact() != null) {
            return SEND_CONTACT;
        }

        return parseCommand(getUpdateData(update));
    }

    public static Command parseCommand(String updateData) {
        String commandText = parseCommandText(updateData);

        if (commandText == null) return NOP;

        for (Command command : values()) {
            if (commandText.equalsIgnoreCase(command.getText()) ||
                    commandText.equals(command.getUri())) {
                return command;
            }
        }

        return NOP;
    }

    private static String getUpdateData(Update update) {
        if (update.hasMessage() && update.getMessage().hasText())
            return update.getMessage().getText();

        if (update.hasCallbackQuery()) return update.getCallbackQuery().getData();

        return null;
    }

    public static Integer parseId(Update update) {
        return parseId(getUpdateData(update));
    }

    public static String parseCommandText(String v) {
        if (v == null) return null;
        int i = v.lastIndexOf('_');
        if (i < 0) return v;
        return i > 0 ? v.substring(0, i) : "";
    }

    public static Integer parseId(String v) {
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
