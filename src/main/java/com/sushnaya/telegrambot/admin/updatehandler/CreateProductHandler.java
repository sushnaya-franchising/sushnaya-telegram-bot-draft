package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import com.sushnaya.telegrambot.admin.state.dialog.ProductCreationDialog;
import org.telegram.telegrambots.api.objects.Update;

import java.nio.ByteBuffer;
import java.util.List;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.CommandUriParamType.CATEGORY_ID_PARAM;
import static com.sushnaya.telegrambot.CommandUriParamType.MENU_ID_PARAM;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.selectCategoryKeyboard;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.selectMenuKeyboard;


public class CreateProductHandler extends SushnayaBotUpdateHandler {

    public CreateProductHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final ByteBuffer payload = Command.parseCommandUriByteBufferPayload(update);

        if (payload == null || payload.remaining() < 5) {
            askMenuToCreateProductIn(update);
            return;
        }

        Byte paramType = payload.get();

        if (paramType == MENU_ID_PARAM) {
            final Menu menu = getMenu(payload);
            askCategoryToCreateProductIn(update, menu);

        } else if (paramType == CATEGORY_ID_PARAM) {
            final MenuCategory category = getCategory(payload);
            createProduct(update, category);
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

    private void askMenuToCreateProductIn(Update update) {
        final List<Menu> menus = bot.getDataStorage().getMenus();

        if (menus == null || menus.isEmpty()) {
            bot.say(update, MESSAGES.productCreationInquireMenuCreation(),
                    true);

        } else if (menus.size() == 1) {
            askCategoryToCreateProductIn(update, menus.get(0));

        } else {
            bot.answer(update, MESSAGES.selectMenuForProductCreation(), selectMenuKeyboard(menus,
                    m -> buildCommandUri(CREATE_PRODUCT, MENU_ID_PARAM, m.getId())
            ));
        }
    }

    private void askCategoryToCreateProductIn(Update update, Menu menu) {
        final List<MenuCategory> categories = menu.getCategories();

        if (categories == null || categories.isEmpty()) {
            bot.say(update, MESSAGES.productCreationInquireCategoryCreation(),
                    true);

        } else if (categories.size() == 1) {
            createProduct(update, categories.get(0));

        } else {
            bot.answer(update, MESSAGES.selectCategoryForProductCreation(),
                    selectCategoryKeyboard(categories,
                            c -> buildCommandUri(CREATE_PRODUCT, CATEGORY_ID_PARAM, c.getId())));
        }
    }

    private void createProduct(Update update, MenuCategory category) {
        // todo: refactor design to avoid new instance creation
        new ProductCreationDialog(bot).ask(update).then((u, product) -> {
            bot.setAdminDefaultState(u);

            category.addProduct(product);
            bot.getDataStorage().saveCategory(category);

            bot.say(u, MESSAGES.productCreationIsSuccessful(product), true);
            bot.say(u, MESSAGES.proposeFurtherCommandsForProductCreation(),
                    bot.getAdminKeyboardFactory().productCreationFurtherCommands(
                            category.getMenu(), category));
        }).onCancel(this::cancelProductCreation);
    }

    private void cancelProductCreation(Update u) {
        bot.setAdminDefaultState(u);

        final Command command = Command.parseCommand(u);

        if (command == CREATE_PRODUCT) return;

        String message = command == CANCEL ? MESSAGES.productCreationIsCancelled() :
                MESSAGES.productCreationIsInterrupted();

        bot.say(u, message, true);
    }
}
