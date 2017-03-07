package com.sushnaya.telegrambot.util;

import com.google.common.collect.Lists;
import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.function.Function;

import static com.sushnaya.telegrambot.Command.*;

public class KeyboardMarkupUtil {
    public static ReplyKeyboardRemove REPLY_KEYBOARD_REMOVE = new ReplyKeyboardRemove();

    private KeyboardMarkupUtil() {
    }

    public static <X> InlineKeyboardMarkup twoColumnsInlineKeyboard(
            List<X> xs, Function<X, String> buttonTextProvider,
            Function<X, String> callbackDataProvider) {

        final List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        List<InlineKeyboardButton> row = null;

        for (int i = 0; i < xs.size(); i++) {
            if (i % 2 == 0) {
                if (row != null) keyboard.add(row);
                row = Lists.newArrayList();
            }

            X x = xs.get(i);

            row.add(new InlineKeyboardButton().setText(buttonTextProvider.apply(x))
                    .setCallbackData(callbackDataProvider.apply(x)));
        }

        keyboard.add(row);

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }

    public static InlineKeyboardMarkup insertButton(InlineKeyboardMarkup markup,
                                                    int rowIndex, int columnIndex,
                                                    String text, String callbackData) {
        final List<InlineKeyboardButton> row = markup.getKeyboard().get(rowIndex);

        row.add(columnIndex, new InlineKeyboardButton().setText(text)
                .setCallbackData(callbackData));

        return markup;
    }

    public static InlineKeyboardMarkup appendButton(InlineKeyboardMarkup markup,
                                                    String text, String callbackData) {
        markup.getKeyboard().add(Lists.newArrayList(
                new InlineKeyboardButton().setText(text).setCallbackData(callbackData)
        ));

        return markup;
    }

    public static ReplyKeyboardMarkup singleButtonOneTimeKeyboard(String text) {
        final List<KeyboardRow> keyboard = Lists.newArrayList();
        final KeyboardRow row = new KeyboardRow();
        row.add(text);
        keyboard.add(row);

        return new ReplyKeyboardMarkup().setKeyboard(keyboard).setResizeKeyboard(true)
                .setOneTimeKeyboad(true);
    }

    public static InlineKeyboardMarkup singleButtonInlineKeyboard(String text, String callbackData) {
        final List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        final List<InlineKeyboardButton> row = Lists.newArrayList();

        row.add(new InlineKeyboardButton().setText(text).setCallbackData(callbackData));
        keyboard.add(row);

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }

    public static ReplyKeyboard singleRequestContactButtonKeyboard(String text) {
        List<KeyboardRow> keyboard = Lists.newArrayList();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton(text);
        button.setRequestContact(true);
        row.add(button);
        keyboard.add(row);

        return new ReplyKeyboardMarkup().setKeyboard(keyboard).setResizeKeyboard(true)
                .setOneTimeKeyboad(true);
    }

    public static InlineKeyboardMarkup selectMenuKeyboard(List<Menu> menus) {
        final Function<Menu, String> callbackDataProvider = m -> buildCommandUri(MENU, m.getId());

        return selectMenuKeyboard(menus, callbackDataProvider);
    }

    public static InlineKeyboardMarkup selectMenuKeyboard(
            List<Menu> menus, Function<Menu, String> callbackDataProvider) {
        final Function<Menu, String> buttonTextProvider = Menu::getLocalityName;
        return twoColumnsInlineKeyboard(menus, buttonTextProvider, callbackDataProvider);
    }

    public static InlineKeyboardMarkup selectCategoryKeyboard(List<MenuCategory> categories) {
        final Function<MenuCategory, String> callbackDataProvider =
                c -> buildCommandUri(NEXT_PRODUCT_IN_CATEGORY, c.getId());
        return selectCategoryKeyboard(categories, callbackDataProvider);
    }

    public static InlineKeyboardMarkup selectCategoryKeyboard(
            List<MenuCategory> categories, Function<MenuCategory, String> callbackDataProvider) {
        final Function<MenuCategory, String> buttonTextProvider = MenuCategory::getDisplayName;
        return twoColumnsInlineKeyboard(categories, buttonTextProvider, callbackDataProvider);
        // todo: add search button when search will be integrated
    }
}
