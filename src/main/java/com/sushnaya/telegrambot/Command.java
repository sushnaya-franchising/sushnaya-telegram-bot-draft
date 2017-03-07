package com.sushnaya.telegrambot;

import org.telegram.telegrambots.api.objects.Update;

import java.nio.ByteBuffer;
import java.util.function.Function;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;

// todo: refactor command design (do not use enum)
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
    PROMOTIONS("/promotions"),
    CREATE_MENU("/newmenu"),
    EDIT_MENU("/editmenu"),
    EDIT_LOCALITY("/editlocality"),
    DELETE_MENU("/deletemenu"),
    CATEGORY("/category"),
    EDIT_CATEGORY("/editcategories"),
    CREATE_CATEGORY("/newcategory"),
    DELETE_CATEGORY("/deletecategory"),
    PRODUCTS("/products"),
    NEXT_PRODUCT_IN_CATEGORY("/nextproductincategory"),
    EDIT_PRODUCTS("/editproducts"),
    SET_PRODUCT_SUBHEADING("/setproductsubheading"),
    SET_PRODUCT_DESCRIPTION("/setproductdescription"),
    PUBLISH_PRODUCT("/publishproduct"),
    CREATE_PRODUCT("/newproduct"),
    CREATE_PRODUCT_IN_CATEGORY("/newproductincategory");

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

    public static String buildCommandUri(Command command, ByteBuffer payload) {
        return buildCommandUri(command, payload.array());
    }

    public static String buildCommandUri(Command command, byte[] payload) {
        final String base64Payload = encodeBase64String(payload).replace("=", "");

        return new StringBuilder(command.getUri()).append('_').append(base64Payload).toString();
    }

    public static Integer parseCommandUriIntPayload(Update update) {
        final ByteBuffer byteBuffer = parseCommandUriByteBufferPayload(update);

        if (byteBuffer == null) return null;

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
