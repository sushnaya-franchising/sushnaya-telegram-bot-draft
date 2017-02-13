package com.sushnaya.telegrambot.state.admin;

import com.google.common.collect.Lists;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.*;
import com.sushnaya.telegrambot.command.admin.*;
import com.sushnaya.telegrambot.command.user.ShowHelp;
import com.sushnaya.telegrambot.command.user.Start;
import com.sushnaya.telegrambot.command.user.UnknownCommand;
import com.sushnaya.telegrambot.state.user.DefaultState;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

import static com.sushnaya.telegrambot.command.Command.ButtonEmojiLabel.*;
import static com.sushnaya.telegrambot.command.Command.Uri.*;

public class AdminOnDefaultState extends DefaultState {
    public static final ReplyKeyboardMarkup KEYBOARD_MARKUP = new ReplyKeyboardMarkup();

    static {
        List<KeyboardRow> keyboard = Lists.newArrayList();
        KeyboardRow row = new KeyboardRow();
        row.add(MANAGEMENT_BUTTON_LABEL);
        row.add(NOTIFICATION_BUTTON_LABEL);
        row.add(STATISTICS_BUTTON_LABEL);
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(ADMIN_OFF_BUTTON_LABEL);
        keyboard.add(row);
        KEYBOARD_MARKUP.setKeyboard(keyboard);
        KEYBOARD_MARKUP.setResizeKeyboard(true);
    }

    private final Command openManagementCommand = createOpenManagementCommand();
    private RevealEditBrand revealEditBrand = new RevealEditBrand(bot);

    public AdminOnDefaultState(SushnayaBot bot) {
        super(bot);
    }

    protected Command getCommand(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (parseCommandUri(update.getMessage().getText())) {
                case MANAGEMENT_URI:
                    return openManagementCommand;
                    // todo: handle /products_{id} /product_categories_{id}
                // todo: ignore self command
                case EDIT_BRAND_URI:
                    return revealEditBrand;
            }
        }


        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            if (data.startsWith(EDIT_BRAND_URI)) {
                return revealEditBrand;
            }
        }

        return super.getCommand(update);
    }

    protected Start createStartCommand() {
        return new AdminStart(bot);
    }

    protected ShowHelp createShowHelpCommand() {
        return new AdminOnShowHelp(bot);
    }

    private Command createOpenManagementCommand() {
        return new RevealManagement(bot);
    }

    private Command createCreateBrandCommand() {
        return new RevealEditBrandNameStep(bot);
    }

    protected UnknownCommand createUnknownCommand() {
        return new AdminUnknownCommand(bot);
    }
}
