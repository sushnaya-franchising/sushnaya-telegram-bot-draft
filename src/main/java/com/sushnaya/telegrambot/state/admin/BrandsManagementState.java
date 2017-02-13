package com.sushnaya.telegrambot.state.admin;

import com.google.common.collect.Lists;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.Command;
import com.sushnaya.telegrambot.command.admin.RevealEditBrandNameStep;
import com.sushnaya.telegrambot.command.admin.RevealManagement;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

import static com.sushnaya.telegrambot.command.Command.ButtonEmojiLabel.BACK_BUTTON_LABEL;
import static com.sushnaya.telegrambot.command.Command.ButtonEmojiLabel.NEW_BRAND_BUTTON_LABEL;
import static com.sushnaya.telegrambot.command.Command.Uri.*;

public class BrandsManagementState extends AdminOnDefaultState {
    public static final ReplyKeyboardMarkup KEYBOARD_MARKUP = new ReplyKeyboardMarkup();

    static {
        List<KeyboardRow> keyboard = Lists.newArrayList();
        KeyboardRow row = new KeyboardRow();
        row.add(NEW_BRAND_BUTTON_LABEL);
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(BACK_BUTTON_LABEL);
        keyboard.add(row);
        KEYBOARD_MARKUP.setKeyboard(keyboard);
        KEYBOARD_MARKUP.setResizeKeyboard(true);
    }

    private final Command openManagementCommand = new RevealManagement(bot);
    private final Command createBrandCommand = new RevealEditBrandNameStep(bot);

    public BrandsManagementState(SushnayaBot bot) {
        super(bot);
    }

    protected Command getCommand(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (parseCommandUri(update.getMessage().getText())) {
                case BACK_URI:
                    return openManagementCommand;

                case NEW_BRAND_URI:
                    return createBrandCommand;
            }
        }

        return super.getCommand(update);
    }
}
