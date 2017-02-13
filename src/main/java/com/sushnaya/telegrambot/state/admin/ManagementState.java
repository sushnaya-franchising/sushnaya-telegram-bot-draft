package com.sushnaya.telegrambot.state.admin;

import com.google.common.collect.Lists;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.Command;
import com.sushnaya.telegrambot.command.admin.RevealBrandsManagement;
import com.sushnaya.telegrambot.command.admin.RevealDefault;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

import static com.sushnaya.telegrambot.command.Command.ButtonEmojiLabel.*;
import static com.sushnaya.telegrambot.command.Command.Uri.*;

public class ManagementState extends AdminOnDefaultState {

    public static final ReplyKeyboardMarkup KEYBOARD_MARKUP = new ReplyKeyboardMarkup();

    static {
        List<KeyboardRow> keyboard = Lists.newArrayList();
        KeyboardRow row = new KeyboardRow();
        row.add(BRANDS_MANAGEMENT_BUTTON_LABEL);
        row.add(MENU_MANAGEMENT_BUTTON_LABEL);
        row.add(DELIVERY_MANAGEMENT_BUTTON_LABEL);
        row.add(USERS_MANAGEMENT_BUTTON_LABEL);
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(BACK_BUTTON_LABEL);
        keyboard.add(row);
        KEYBOARD_MARKUP.setKeyboard(keyboard);
        KEYBOARD_MARKUP.setResizeKeyboard(true);
    }

    private final Command backToDefaultStateCommand = new RevealDefault(bot);
    private final Command openBrandsManagementCommand = new RevealBrandsManagement(bot);

    public ManagementState(SushnayaBot bot) {
        super(bot);
    }

    protected Command getCommand(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (parseCommandUri(update.getMessage().getText())) {
                case BACK_URI:
                    return backToDefaultStateCommand;

                case BRANDS_MANAGEMENT_URI:
                    return openBrandsManagementCommand;
            }
        }

        return super.getCommand(update);
    }
}
