package com.sushnaya.telegrambot.user.keyboard;

import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import com.sushnaya.entity.Product;
import com.sushnaya.telegrambot.KeyboardMarkupFactory;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

import static com.sushnaya.telegrambot.Command.MENU;
import static com.sushnaya.telegrambot.Command.NEXT_PRODUCT_IN_CATEGORY;
import static com.sushnaya.telegrambot.Command.buildCommandUri;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.selectCategoryKeyboard;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.selectMenuKeyboard;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.singleButtonInlineKeyboard;

public class UserKeyboardMarkupFactory implements KeyboardMarkupFactory {
    @Override
    public InlineKeyboardMarkup menuCategoriesKeyboard(List<MenuCategory> categories) {
        return selectCategoryKeyboard(categories);
    }

    @Override
    public InlineKeyboardMarkup menusKeyboard(List<Menu> menus) {
        return selectMenuKeyboard(menus);
    }

    @Override
    public InlineKeyboardMarkup nextProductInCategoryKeyboard(Product product, int cursor, int productsCount) {
        final int categoryId = product.getCategory().getId();
        final int nextProductCursor = cursor + 1;

        if (nextProductCursor >= productsCount) {
            return singleButtonInlineKeyboard(MESSAGES.menu(), MENU.getUri());

        } else {
            final String commandUri = buildCommandUri(NEXT_PRODUCT_IN_CATEGORY,
                    categoryId, nextProductCursor);

            return singleButtonInlineKeyboard(MESSAGES.more(), commandUri);
        }
    }
}
