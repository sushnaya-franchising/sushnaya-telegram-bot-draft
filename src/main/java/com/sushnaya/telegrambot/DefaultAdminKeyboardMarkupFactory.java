package com.sushnaya.telegrambot;

import com.google.common.collect.Lists;
import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.sushnaya.telegrambot.Command.ADMIN_DASHBOARD;
import static com.sushnaya.telegrambot.Command.BACK_TO_DASHBOARD;
import static com.sushnaya.telegrambot.Command.BACK_TO_HOME;

public class DefaultAdminKeyboardMarkupFactory extends StartupAdminKeyboardMarkupFactory {
    private static final KeyboardMarkupFactory DEFAULT_KEYBOARD_MARKUP_FACTORY =
            new DefaultKeyboardMarkupFactory();

    public InlineKeyboardMarkup homeMarkup() {
        return appendButton(DEFAULT_KEYBOARD_MARKUP_FACTORY.homeMarkup(),
                ADMIN_DASHBOARD.getText(), ADMIN_DASHBOARD.getUri());
    }

    public InlineKeyboardMarkup dashboardMarkup() {
        return appendBackToHomeButton(super.dashboardMarkup());
    }

    public InlineKeyboardMarkup localityAlreadyBoundToMenuMarkup() {
        return addBackToHomeButton(super.localityAlreadyBoundToMenuMarkup());
    }

    public InlineKeyboardMarkup proposalToAddOneMoreProductMarkup(MenuCategory category) {
        return addBackToHomeButton(super.proposalToAddOneMoreProductMarkup(category));
    }

    public InlineKeyboardMarkup editMenus(List<Menu> menus) {
        return addBackToHomeButton(super.editMenus(menus));
    }

    private InlineKeyboardMarkup appendBackToHomeButton(InlineKeyboardMarkup markup) {
        return appendButton(markup, BACK_TO_HOME.getText(), BACK_TO_HOME.getUri());
    }

    private InlineKeyboardMarkup addBackToHomeButton(InlineKeyboardMarkup markup) {
        return addButton(markup, BACK_TO_HOME.getText(), BACK_TO_HOME.getUri());
    }

    private InlineKeyboardMarkup appendButton(InlineKeyboardMarkup markup, String label, String commandUri) {
        markup.getKeyboard().add(Lists.newArrayList(
                new InlineKeyboardButton().setText(label).setCallbackData(commandUri)
        ));

        return markup;
    }

    private InlineKeyboardMarkup addButton(InlineKeyboardMarkup markup, String label, String commandUri) {
        final int lastRowIdx = markup.getKeyboard().size() - 1;

        markup.getKeyboard().get(lastRowIdx).add(
                new InlineKeyboardButton().setText(label).setCallbackData(commandUri)
        );

        return markup;
    }
}
