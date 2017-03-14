package com.sushnaya.telegrambot;

import org.telegram.telegrambots.api.objects.Update;

import java.nio.ByteBuffer;
import java.util.function.Function;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;

public enum Command {
    NOP("/nop"),
    SEND_CONTACT("/sendcontact"),
    START("/start"),
    HELP("/help"),
    MENU("/menu"),
    SEARCH("/search"),
    ADMIN_DASHBOARD("/admin"),
    CANCEL("/cancel"),
    SKIP("/skip"),
    CONTINUE("/continue"),
    BACK_TO_MENU("/backtomenu"),
    SETTINGS("/settings"),
    STATISTICS("/stat"),
    NOTIFY("/notify"),
    PROMOTION("/promotion"),
    CREATE_MENU("/newmenu"),
    EDIT_MENU("/editmenu"),
    EDIT_MENU_DELIVERY("/editmenudelivery"),
    EDIT_MENU_AFFILIATES("/editmenuaffiliates"),
    EDIT_LOCALITY("/editlocality"),
    DELETE_MENU("/deletemenu"),
    CLONE_MENU("/clonemenu"),
    RECOVER_MENU("/recovermenu"),
    CATEGORY("/category"),
    CREATE_CATEGORY("/newcategory"),
    EDIT_CATEGORY("/editcategory"),
    SET_CATEGORY_NAME("/setcategoryname"),
    SET_CATEGORY_SUBHEADING("/setcategorysubheading"),
    SET_CATEGORY_PHOTO("/setcategoryphoto"),
    DELETE_CATEGORY("/deletecategory"),
    RECOVER_CATEGORY("/recovercategory"),
    NEXT_PRODUCT_IN_CATEGORY("/nextproductincategory"),
    CREATE_PRODUCT("/newproduct"),
    EDIT_PRODUCT("/editproduct"),
    DELETE_PRODUCT("/deleteproduct"),
    SET_PRODUCT_NAME("/setproductname"),
    SET_PRODUCT_PRICE("/setproductprice"),
    SET_PRODUCT_SUBHEADING("/setproductsubheading"),
    SET_PRODUCT_DESCRIPTION("/setproductdescription"),
    SET_PRODUCT_PHOTO("/setproductphoto"),
    EDIT_PRODUCT_MODIFICATIONS("/editproductmodifications"),
    EDIT_PRODUCT_OPTIONS("/editproductoptions"),
    RECOVER_PRODUCT("/recoverproduct"),
    HIDE_PRODUCT("/hideproduct"),
    PUBLISH_PRODUCT("/publishproduct"),
    DELETE_OPTIONAL_PROPERTY("/deleteoptionalproperty");

    private Function<String, String> textSupplier;
    private String uri;

    Command(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public static Command parseCommand(Update update) {
        return parseCommand(getCommandUri(update));
    }

    private static Command parseCommand(String s) {
        if (s == null) return NOP;

        for (Command command : values()) {
            if (command.getUri().equalsIgnoreCase(parseCommandUriPath(s))) {
                return command;
            }
        }

        return NOP;
    }

    public static String parseCommandUriPath(String commandUri) {
        if (commandUri == null) return null;
        int i = commandUri.lastIndexOf('_');
        if (i < 0) return commandUri;
        return i > 0 ? commandUri.substring(0, i) : "";
    }

    private static String getCommandUri(Update update) {
        if (update.hasMessage() && update.getMessage().hasText())
            return update.getMessage().getText();

        if (update.hasCallbackQuery()) return update.getCallbackQuery().getData();

        return null;
    }

    public static String buildCommandUri(Command command, int payload) {
        return buildCommandUri(command, ByteBuffer.allocate(4).putInt(payload));
    }

    public static String buildCommandUri(Command command, byte arg1, int arg2) {
        return buildCommandUri(command, ByteBuffer.allocate(5).put(arg1).putInt(arg2));
    }

    public static String buildCommandUri(Command command, int arg1, int arg2) {
        return buildCommandUri(command, ByteBuffer.allocate(8).putInt(arg1).putInt(arg2));
    }

    public static String buildCommandUri(Command command, ByteBuffer payload) {
        return buildCommandUri(command, payload.array());
    }

    public static String buildCommandUri(Command command, byte[] payload) {
        final String base64Payload = encodeBase64String(payload).replace("=", "");

        return new StringBuilder(command.getUri()).append('_').append(base64Payload).toString();
    }

    public static Integer parseCommandUriIntPayload(Update update) {
        final ByteBuffer byteBuffer = parseCommandUriByteBufferPayload(update);

        if (byteBuffer == null || byteBuffer.remaining() < 4) return null;

        return byteBuffer.getInt();
    }

    public static ByteBuffer parseCommandUriByteBufferPayload(Update update) {
        return parseCommandUriByteBufferPayload(getCommandUri(update));
    }

    public static ByteBuffer parseCommandUriByteBufferPayload(String commandUri) {
        final byte[] payload = parseCommandUriPayload(commandUri);

        if (payload == null) return null;

        return ByteBuffer.wrap(payload);
    }

    public static byte[] parseCommandUriPayload(Update update) {
        return parseCommandUriPayload(getCommandUri(update));
    }

    public static byte[] parseCommandUriPayload(String commandUri) {
        if (commandUri == null) return null;
        int i = commandUri.lastIndexOf('_');

        return i > 0 ? decodeBase64(commandUri.substring(i + 1, commandUri.length())) : null;
    }
}
