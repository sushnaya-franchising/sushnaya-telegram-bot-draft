package com.sushnaya.telegrambot.command.admin;

import com.google.common.collect.Lists;
import com.sushnaya.entity.Brand;
import com.sushnaya.telegrambot.Data;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.ReplyCommand;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

import static com.sushnaya.telegrambot.command.Command.Uri.DELETE_BRAND_URI;
import static com.sushnaya.telegrambot.command.Command.Uri.EDIT_BRAND_NAME_URI;

public class RevealEditBrand extends ReplyCommand {
    public RevealEditBrand(SushnayaBot bot) {
        super(bot);
    }

    public void execute(Update update) {
        if(update.hasCallbackQuery()) {
            Brand brand = getBrand(update.getCallbackQuery().getData());
            InlineKeyboardMarkup keyboardMarkup = buildKeyboard(brand);

            try {
                bot.editMessageText(new EditMessageText()
                        .setChatId(update.getCallbackQuery().getMessage().getChatId())
                        .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                        .setReplyMarkup(keyboardMarkup)
                        .setParseMode(ParseMode.HTML)
                        .setText("Что нужно сделать с <b>" + brand.getName() + "</b>? ✏️")
                        .setReplyMarkup(keyboardMarkup)
                );
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            super.execute(update);
        }
    }

    protected SendMessage createSendMessage(Update update) {
        Brand brand = getBrand(update.getMessage().getText());
        InlineKeyboardMarkup keyboardMarkup = buildKeyboard(brand);

        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setReplyMarkup(keyboardMarkup)
                .setParseMode(ParseMode.HTML)
                .setText("\uD83D\uDCDD Редактирование брэнда \"<b>" + brand.getName() + "</b>\"")
                .setReplyMarkup(keyboardMarkup);
    }

    private Brand getBrand(String editBrandUri) {
        //todo: secure parsing
        int brandId = Integer.parseInt(editBrandUri.split("_")[1], 16);
        return Data.get().getBrand(brandId);
    }

    private InlineKeyboardMarkup buildKeyboard(Brand brand) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        List<InlineKeyboardButton> row = Lists.newArrayList(
                new InlineKeyboardButton().setText("Редактировать название").setCallbackData(EDIT_BRAND_NAME_URI + brand.getId()),
                new InlineKeyboardButton().setText("Удалить").setCallbackData(DELETE_BRAND_URI + brand.getId())
        );
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }
}
