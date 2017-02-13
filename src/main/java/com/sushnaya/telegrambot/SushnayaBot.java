package com.sushnaya.telegrambot;

import com.google.common.collect.Maps;
import com.sushnaya.entity.User;
import com.sushnaya.telegrambot.state.SushnayaBotState;
import com.sushnaya.telegrambot.state.admin.AdminOnDefaultState;
import com.sushnaya.telegrambot.state.user.DefaultState;
import com.sushnaya.telegrambot.state.user.UnregisteredState;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.Map;

public class SushnayaBot extends TelegramLongPollingBot {
    private static final Data DATA = Data.get();
    private static final Map<Integer, SushnayaBotState> STATES_BY_USER_ID = Maps.newHashMap();
    private final String token;

    public SushnayaBot(String token) {
        this.token = token;
    }

    public void setState(Integer userId, SushnayaBotState state) {
        STATES_BY_USER_ID.put(userId, state);
    }

    public boolean isRegistered(Integer userId) {
        if (userId == null) return false;

        User user = DATA.getUser(userId);
        return user != null && user.getTelegramId() != null;
    }

    public boolean isAdmin(Integer userId) {
        if (userId == null) return false;

        return DATA.getAdmin(userId) != null;
    }

    public void registerUser(User user) {
        if (StringUtils.isEmpty(user.getPhoneNumber())) {
            throw new Error("User phone number must be provided");
        }

        DATA.saveUser(user);
    }

    public void onUpdateReceived(Update update) {
        Integer id = null;

        if (update.hasInlineQuery()) {
            id = update.getInlineQuery().getFrom().getId();
        }

        if (update.hasEditedMessage()) {
            id = update.getEditedMessage().getFrom().getId();
        }

        if (update.hasCallbackQuery()) {
            id = update.getCallbackQuery().getFrom().getId();
        }

        if (update.hasMessage()) {
            id = update.getMessage().getFrom().getId();
        }

        if (id != null) {
            ensureBotState(id).process(update);
        }
    }

    private SushnayaBotState ensureBotState(Integer id) {
        SushnayaBotState botState = STATES_BY_USER_ID.get(id);

        if (botState == null) {
            botState = isAdmin(id) ? new AdminOnDefaultState(this) :
                    isRegistered(id) ? new DefaultState(this) :
                            new UnregisteredState(this);
            STATES_BY_USER_ID.put(id, botState);
        }

        return botState;
    }

    public String getBotUsername() {
        return "sushnayabot";
    }

    public String getBotToken() {
        return token;
    }
}
