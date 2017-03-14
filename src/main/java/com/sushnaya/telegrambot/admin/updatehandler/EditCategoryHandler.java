package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.CommandUriParamType.CATEGORY_ID_PARAM;
import static com.sushnaya.telegrambot.CommandUriParamType.MENU_ID_PARAM;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.selectCategoryKeyboard;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.selectMenuKeyboard;

public class EditCategoryHandler extends SushnayaBotUpdateHandler {

    public EditCategoryHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final ByteBuffer payload = Command.parseCommandUriByteBufferPayload(update);

        if (payload == null || payload.remaining() < 5) {
            askMenuToEditCategoryIn(update);
            return;
        }

        Byte paramType = payload.get();

        if (paramType == MENU_ID_PARAM) {
            final Menu menu = getMenu(payload);
            askCategoryToEdit(update, menu);

        } else if (paramType == CATEGORY_ID_PARAM) {
            final MenuCategory category = getCategory(payload);
            editCategory(update, category);

        } else {
            bot.handleUnknownCommand(update);
        }
    }

    private Menu getMenu(ByteBuffer payload) {
        final int menuId = payload.getInt();

        return bot.getDataStorage().getMenu(menuId);
    }

    private MenuCategory getCategory(ByteBuffer payload) {
        final int categoryId = payload.getInt();

        return bot.getDataStorage().getMenuCategory(categoryId);
    }

    private void askMenuToEditCategoryIn(Update update) {
        final List<Menu> menus = bot.getDataStorage().getMenus()
                .stream().filter(m -> !m.getCategories().isEmpty())
                .collect(Collectors.toList());

        if (menus == null || menus.isEmpty()) {
            bot.say(update, MESSAGES.noCategoryWasCreatedToEdit(CREATE_CATEGORY), true);

        } else if (menus.size() == 1) {
            askCategoryToEdit(update, menus.get(0));

        } else {
            bot.answer(update, MESSAGES.selectMenuToEditCategoryIn(), selectMenuKeyboard(menus,
                    m -> buildCommandUri(EDIT_CATEGORY, MENU_ID_PARAM, m.getId())
            ));
        }
    }

    private void askCategoryToEdit(Update update, Menu menu) {
        final List<MenuCategory> categories = menu.getCategories();

        if (categories == null || categories.isEmpty()) {
            bot.say(update, MESSAGES.noCategoryWasCreatedToEdit(CREATE_CATEGORY),
                    true);

        } else if (categories.size() == 1) {
            editCategory(update, categories.get(0));

        } else {
            bot.answer(update, MESSAGES.selectCategoryToEdit(),
                    selectCategoryKeyboard(categories,
                            c -> buildCommandUri(EDIT_CATEGORY, CATEGORY_ID_PARAM, c.getId())));
        }
    }

    private void editCategory(Update update, MenuCategory category) {
        bot.answer(update, getEditCategoryMessageText(category), getEditCategoryKeyboard(category));
    }

    protected String getEditCategoryMessageText(MenuCategory category) {
        return MESSAGES.categorySettings(category);
    }

    protected InlineKeyboardMarkup getEditCategoryKeyboard(MenuCategory category) {
        return bot.getAdminKeyboardFactory().editCategory(category);
    }
}
