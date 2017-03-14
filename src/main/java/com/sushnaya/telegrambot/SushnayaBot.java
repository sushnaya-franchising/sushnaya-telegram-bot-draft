package com.sushnaya.telegrambot;

import com.google.common.collect.Maps;
import com.sushnaya.entity.Coordinate;
import com.sushnaya.entity.Locality;
import com.sushnaya.entity.Menu;
import com.sushnaya.entity.User;
import com.sushnaya.telegrambot.admin.keyboard.AdminKeyboardMarkupFactory;
import com.sushnaya.telegrambot.admin.state.AdminDefaultState;
import com.sushnaya.telegrambot.user.keyboard.UserKeyboardMarkupFactory;
import com.sushnaya.telegrambot.user.state.UnregisteredUserState;
import com.sushnaya.telegrambot.user.state.UserDefaultState;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendLocation;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.File;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.REPLY_KEYBOARD_REMOVE;
import static com.sushnaya.telegrambot.util.UpdateUtil.*;

public class SushnayaBot extends TelegramLongPollingBot {
    private static final Map<Integer, BotState> STATES_BY_TELEGRAM_USER_ID = Maps.newHashMap();
    public static final Messages MESSAGES = Messages.getDefaultMessages();
    private final String token;
    private final HttpClient httpClient;
    private final DataStorage dataStorage;
    private final UnregisteredUserState unregisteredUserState;
    private final UserDefaultState userDefaultState;
    private final AdminDefaultState adminDefaultState;

    public SushnayaBot(String token) {
        this.token = token;

        httpClient = HttpClientBuilder.create()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setConnectionTimeToLive(70, TimeUnit.SECONDS)
                .setMaxConnTotal(100)
                .build();

        dataStorage = DataStorage.get();

        unregisteredUserState = new UnregisteredUserState(this);
        userDefaultState = new UserDefaultState(this);
        adminDefaultState = new AdminDefaultState(this);
    }

    public KeyboardMarkupFactory getKeyboardFactory(User user) {
        return KeyboardFactoryProvider.getKeyboardFactory(user, this);
    }

    public AdminKeyboardMarkupFactory getAdminKeyboardFactory() {
        return KeyboardFactoryProvider.getAdminKeyboardFactory(this);
    }

    public UserKeyboardMarkupFactory getUserKeyboardFactory() {
        return KeyboardFactoryProvider.getUserKeyboardFactory();
    }

    public UnregisteredUserState setUnregisteredState(Update update) {
        return setUnregisteredState(getTelegramUserId(update));
    }

    public UnregisteredUserState setUnregisteredState(int userId) {
        setState(userId, unregisteredUserState);
        return unregisteredUserState;
    }

    public SushnayaBot setUserDefaultState(Update update) {
        return setUserDefaultState(getTelegramUserId(update));
    }

    public SushnayaBot setUserDefaultState(int userId) {
        setState(userId, userDefaultState);
        return this;
    }

    public SushnayaBot setAdminDefaultState(Update update) {
        return setAdminDefaultState(getTelegramUserId(update));
    }

    public SushnayaBot setAdminDefaultState(int userId) {
        return setState(userId, adminDefaultState);
    }

    public SushnayaBot setState(Update update, BotState state) {
        return setState(getTelegramUserId(update), state);
    }

    public SushnayaBot setState(int userId, BotState state) {
        STATES_BY_TELEGRAM_USER_ID.put(userId, state);
        return this;
    }

    public boolean isRegistered(Integer userId) {
        if (userId == null) return false;

        User user = dataStorage.getUserByTelegramId(userId);
        return user != null && user.getTelegramId() != null;
    }

    public boolean isAdmin(Integer userId) {
        if (userId == null) return false;

        return dataStorage.getAdmin(userId) != null;
    }

    public DataStorage getDataStorage() {
        return dataStorage;
    }

    public boolean hasMenu() {
        return !getDataStorage().getMenus().isEmpty();
    }

    public boolean hasProducts() {
        return getDataStorage().hasProducts();
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void onUpdateReceived(Update update) {
        ensureBotState(getTelegramUserId(update)).handle(update);
    }

    public void revealMenu(Update update) {
        handleCommand(update, MENU);
    }

    public void cancelCurrentOperation(Update update) {
        handleCommand(update, CANCEL);
    }

    public void revealHelp(Update update) {
        handleCommand(update, HELP);
    }

    public void handleCommand(Update update, Command command) {
        ensureBotState(getTelegramUserId(update)).handle(update, command);
    }

    private BotState ensureBotState(Integer telegramUserId) {
        BotState botState = STATES_BY_TELEGRAM_USER_ID.get(telegramUserId);

        if (botState == null) {
            botState = isAdmin(telegramUserId) ? adminDefaultState :
                    isRegistered(telegramUserId) ? userDefaultState :
                            unregisteredUserState;

            STATES_BY_TELEGRAM_USER_ID.put(telegramUserId, botState);
        }

        return botState;
    }

    public String getBotUsername() {
        return "sushnayabot";
    }

    public String getBotToken() {
        return token;
    }

    public boolean isLocalityAlreadyBoundToMenu(Locality locality) {
        return dataStorage.isLocalityAlreadyBoundToMenu(locality);
    }

    public String getFilePath(PhotoSize photo) throws TelegramApiException {
        Objects.requireNonNull(photo);

        if (photo.hasFilePath()) return photo.getFilePath();

        GetFile getFileMethod = new GetFile();
        getFileMethod.setFileId(photo.getFileId());

        File file = getFile(getFileMethod);

        return file.getFilePath();
    }

    public void handleUnknownCommand(Update update) {
        say(update, MESSAGES.userUnknownCommand());
    }

    public void say(Update update, String message) {
        say(update, message, false);
    }

    public void say(Update update, String message, boolean removeKeyboard) {
        say(update, message, removeKeyboard ? REPLY_KEYBOARD_REMOVE : null);
    }

    public void say(Update update, String message, ReplyKeyboard keyboard) {
        try {
            sendMessage(new SendMessage()
                    .setChatId(getChatId(update))
                    .setParseMode(ParseMode.HTML)
                    .setReplyMarkup(keyboard)
                    .setText(message));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void edit(Update update, String message, InlineKeyboardMarkup keyboard) {
        try {
            editMessageText(new EditMessageText()
                    .setChatId(getChatId(update))
                    .setMessageId(getMessageId(update))
                    .setParseMode(ParseMode.HTML)
                    .setReplyMarkup(keyboard)
                    .setText(message));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void say(Update update, Coordinate coordinate) {
        try {
            sendLocation(new SendLocation()
                    .setChatId(getChatId(update))
                    .setLongitude(coordinate.getLongitude())
                    .setLatitude(coordinate.getLatitude()));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void answer(Update update, String message, InlineKeyboardMarkup keyboard) {
        if (update.hasCallbackQuery()) {
            edit(update, message, keyboard);

        } else {
            say(update, message, keyboard);
        }
    }

    public boolean hasPublishedProducts() {
        return getDataStorage().hasPublishedProducts();
    }

    public boolean hasPublishedProducts(int menuId) {
        return getDataStorage().hasPublishedProducts(menuId);
    }

    public List<Menu> getMenusWithPublishedProducts() {
        return getDataStorage().getMenusWithPublishedProducts();
    }
}
