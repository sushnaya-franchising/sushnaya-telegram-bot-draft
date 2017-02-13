package com.sushnaya.telegrambot.command.admin;

import com.google.common.collect.Lists;
import com.sushnaya.entity.Brand;
import com.sushnaya.telegrambot.Data;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.ReplyCommand;
import com.sushnaya.telegrambot.state.admin.BrandsManagementState;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collection;
import java.util.List;

import static com.sushnaya.telegrambot.command.Command.Uri.EDIT_BRAND_URI;
import static com.sushnaya.telegrambot.command.Command.Uri.NEW_BRAND_URI;

public class RevealBrandsManagement extends ReplyCommand {
    private static final Data DATA = Data.get();

    public RevealBrandsManagement(SushnayaBot bot) {
        super(bot);
    }

    public void execute(Update update) {
        super.execute(update);

        sendBrandsList(update);

        bot.setState(update.getMessage().getFrom().getId(), new BrandsManagementState(bot));
    }

    protected SendMessage createSendMessage(Update update) {
        StringBuilder messageBuilder = new StringBuilder()
                .append(getBrandsString());

        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setReplyMarkup(BrandsManagementState.KEYBOARD_MARKUP)
                .setParseMode(ParseMode.HTML)
                .setText(messageBuilder.toString());
    }

    private String getBrandsString() {
        StringBuilder b = new StringBuilder();

        Collection<Brand> brands = DATA.getBrands();

        if (brands.isEmpty()) {
            b.append("A список ваших брэндов пуст.\n\n<i>Создайте первый брэнд</i> - ")
                    .append(NEW_BRAND_URI)
                    .append("\n\n");

        } else {
            int brandsCount = brands.size();
            b.append("Вот список ваших брэндов (всего <b>")
                    .append(brandsCount)
                    .append("</b>):");
        }

        return b.toString();
    }

    private void sendBrandsList(Update update) {
        Collection<Brand> brands = DATA.getBrands();

        if (brands.isEmpty()) return;

        InlineKeyboardMarkup keyboardMarkup = buildKeyboard(brands);

        sendMessage(new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText("Чтобы редактировать брэнд, нажмите на него \uD83D\uDC47")
                .setReplyMarkup(keyboardMarkup)
        );
    }

    private InlineKeyboardMarkup buildKeyboard(Collection<Brand> brands) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();

        List<InlineKeyboardButton> row = null;
        int i = 0;
        for (Brand brand : brands) {
            if (i % 2 == 0) {
                if (row != null) {
                    keyboard.add(row);
                }
                row = Lists.newLinkedList();
            }
            row.add(createBrandButton(brand));
            i++;
        }

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }

    private InlineKeyboardButton createBrandButton(Brand brand) {
        String name = brand.getName();
        String uri = EDIT_BRAND_URI + brand.getIdAsHexString();

        return new InlineKeyboardButton()
                .setText(name)
                .setCallbackData(uri);
    }

}
