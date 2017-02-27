package com.sushnaya.telegrambot;

import com.sushnaya.entity.Locality;
import com.sushnaya.entity.MenuCategory;
import com.sushnaya.telegrambot.util.UTF8Control;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {
    private static Messages DEFAULT_MESSAGES;

    private final ResourceBundle messages;
    private final Locale locale;
    private final MessageFormat createProductInCategoryFormat;
    private final MessageFormat cancelMenuCreationHelpFormat;
    private final MessageFormat askLocalityConfirmationForMenuCreationFormat;
    private final MessageFormat askProductNameForMenuCreationFormat;
    private final MessageFormat valueConfirmationFormat;
    private final MessageFormat skipCategoryPhotoStepHelpFormat;
    private final MessageFormat skipCategorySubheadingStepHelpFormat;
    private final MessageFormat askCategoryPhotoForCategoryCreationFormat;
    private final MessageFormat skipProductPhotoStepHelpFormat;
    private final MessageFormat skipProductSubheadingStepHelpFormat;
    private final MessageFormat skipProductDescriptionStepHelpFormat;
    private final MessageFormat userHelpFormat;
    private final MessageFormat userUnknownCommandFormat;
    private final MessageFormat dashboardDefaultMessageFormat;
    private final MessageFormat menuCreationIsCancelledFormat;

    public static Messages getDefaultMessages() {
        if (DEFAULT_MESSAGES == null) {
            DEFAULT_MESSAGES = create();
        }

        return DEFAULT_MESSAGES;
    }

    public Locale getLocale() {
        return locale;
    }

    public static Messages create() {
        return create(Locale.getDefault());
    }

    public static Messages create(Locale locale) {
        return new Messages(locale);
    }

    private Messages(Locale locale) {
        this.locale = locale;

        this.messages = ResourceBundle.getBundle("MessagesBundle", locale, new UTF8Control());

        createProductInCategoryFormat = new MessageFormat(
                messages.getString("create_product_in_category"), locale);
        cancelMenuCreationHelpFormat = new MessageFormat(
                messages.getString("help.cancel_menu_creation"), locale);
        askLocalityConfirmationForMenuCreationFormat = new MessageFormat(
                messages.getString("menu_creation.ask_locality_confirmation"), locale);
        askProductNameForMenuCreationFormat = new MessageFormat(
                messages.getString("menu_creation.ask_product_name"), locale);
        valueConfirmationFormat = new MessageFormat(messages.getString("value_confirmation"), locale);
        skipCategoryPhotoStepHelpFormat = new MessageFormat(
                messages.getString("help.skip_category_photo_step"), locale);
        skipCategorySubheadingStepHelpFormat = new MessageFormat(
                messages.getString("help.skip_category_subheading_step"), locale);
        askCategoryPhotoForCategoryCreationFormat = new MessageFormat(
                messages.getString("category_creation.ask_category_photo"), locale);
        skipProductPhotoStepHelpFormat = new MessageFormat(
                messages.getString("help.skip_product_photo_step"), locale);
        skipProductSubheadingStepHelpFormat = new MessageFormat(
                messages.getString("help.skip_product_subheading_step"), locale);
        skipProductDescriptionStepHelpFormat = new MessageFormat(
                messages.getString("help.skip_product_description_step"), locale);
        userHelpFormat = new MessageFormat(messages.getString("user.help"), locale);
        userUnknownCommandFormat = new MessageFormat(
                messages.getString("user.unknown_command"), locale);
        dashboardDefaultMessageFormat = new MessageFormat(
                messages.getString("admin.dashboard.default_message"), locale);
        menuCreationIsCancelledFormat = new MessageFormat(
                messages.getString("menu_creation_is_cancelled"), locale);
    }

    public String askProductPriceForProductCreation() {
        return messages.getString("product_creation.ask_product_price");
    }

    public String askProductPhotoForProductCreation() {
        return messages.getString("product_creation.ask_product_photo");
    }

    public String askLocality() {
        return messages.getString("ask_locality");
    }

    public String askLocalityForMenuCreation() {
        return messages.getString("menu_creation.ask_locality");
    }

    public String sendContact() {
        return messages.getString("send_contact");
    }

    public String menu() {
        return messages.getString("menu");
    }

    public String search() {
        return messages.getString("search");
    }

    public String dashboard() {
        return messages.getString("dashboard");
    }

    public String cancel() {
        return messages.getString("cancel");
    }

    public String skip() {
        return messages.getString("skip");
    }

    public String continueMessage() {
        return messages.getString("continue");
    }

    public String backToHome() {
        return messages.getString("back_to_home");
    }

    public String backToDashboard() {
        return messages.getString("back_to_dashboard");
    }

    public String settings() {
        return messages.getString("settings");
    }

    public String statistics() {
        return messages.getString("statistics");
    }

    public String notify_() {
        return messages.getString("notify");
    }

    public String promotions() {
        return messages.getString("promotions");
    }

    public String createMenu() {
        return messages.getString("create_menu");
    }

    public String editMenu() {
        return messages.getString("edit_menu");
    }

    public String editLocality() {
        return messages.getString("edit_locality");
    }

    public String deleteMenu() {
        return messages.getString("delete_menu");
    }

    public String editCategories() {
        return messages.getString("edit_categories");
    }

    public String createCategory() {
        return messages.getString("create_category");
    }

    public String deleteCategory() {
        return messages.getString("delete_category");
    }

    public String editProducts() {
        return messages.getString("edit_products");
    }

    public String setProductSubheading() {
        return messages.getString("set_product_subheading");
    }

    public String setProductDescription() {
        return messages.getString("set_product_description");
    }

    public String publishProduct() {
        return messages.getString("publish_product");
    }

    public String skipProductPublication() {
        return messages.getString("skip_product_publication");
    }

    public String createProductInMenu() {
        return messages.getString("create_product_in_menu");
    }

    public String createProductInCategory(MenuCategory category) {
        Object[] args = {category.getDisplayName()};
        return createProductInCategoryFormat.format(args);
    }

    public String createProductInCurrentCategory() {
        return messages.getString("create_product_in_current_category");
    }

    public String askLocalityHelp() {
        return messages.getString("help.ask_locality");
    }

    public String cancelMenuCreationHelp(Command cancelCommand) {
        Object[] args = {cancelCommand.getUri()};
        return cancelMenuCreationHelpFormat.format(args);
    }

    public String askLocalityConfirmationForMenuCreation(Locality locality) {
        Object[] args = {locality.getDisplayName()};
        return askLocalityConfirmationForMenuCreationFormat.format(args);
    }

    public String askProductNameForMenuCreation(MenuCategory category) {
        Object[] args = {category.getDisplayName()};
        return askProductNameForMenuCreationFormat.format(args);
    }

    public String askCategoryNameForMenuCreation() {
        return messages.getString("menu_creation.ask_category_name");
    }

    public String askCategoryNameForFirstMenuCreation() {
        return messages.getString("first_menu_creation.ask_category_name");
    }

    public String valueConfirmation(String value) {
        Object[] args = {value};
        return valueConfirmationFormat.format(args);
    }

    public String askCategoryName() {
        return messages.getString("ask_category_name");
    }

    public String askCategoryNameHelp() {
        return messages.getString("help.ask_category_name");
    }

    public String askCategoryPhoto() {
        return messages.getString("ask_category_photo");
    }

    public String skipCategoryPhotoStepHelp(Command skipCommand) {
        Object[] args = {skipCommand.getUri()};
        return skipCategoryPhotoStepHelpFormat.format(args);
    }

    public String askCategorySubheading() {
        return messages.getString("ask_category_subheading");
    }

    public String skipCategorySubheadingStepHelp(Command skipCommand) {
        Object[] args = {skipCommand.getUri()};
        return skipCategorySubheadingStepHelpFormat.format(args);
    }

    public String askCategorySubheadingStepHelp() {
        return messages.getString("help.ask_category_subheading");
    }

    public String askCategorySubheadingForCategoryCreation() {
        return messages.getString("category_creation.ask_category_subheading");
    }

    public String askCategoryPhotoForCategoryCreation(MenuCategory category) {
        Object[] args = {category.getDisplayName()};
        return askCategoryPhotoForCategoryCreationFormat.format(args);
    }

    public String askProductName() {
        return messages.getString("ask_product_name");
    }


    public String askProductPrice() {
        return messages.getString("ask_product_price");
    }

    public String askProductPriceHelp() {
        return messages.getString("help.ask_product_price");
    }

    public String askProductPhoto() {
        return messages.getString("ask_product_photo");
    }

    public String skipProductPhotoStepHelp(Command skipCommand) {
        Object[] args = {skipCommand.getUri()};
        return skipProductPhotoStepHelpFormat.format(args);
    }

    public String askProductSubheading() {
        return messages.getString("ask_product_subheading");
    }

    public String askProductSubheadingHelp() {
        return messages.getString("help.ask_product_subheading");
    }

    public String skipProductSubheadingStepHelp(Command skipCommand) {
        Object[] args = {skipCommand.getUri()};
        return skipProductSubheadingStepHelpFormat.format(args);
    }

    public String askProductDescription() {
        return messages.getString("ask_product_description");
    }

    public String skipProductDescriptionStepHelp(Command skipCommand) {
        Object[] args = {skipCommand.getUri()};
        return skipProductDescriptionStepHelpFormat.format(args);
    }

    public String proposeSetProductSubheadingDescriptionAndPublish() {
        return messages.getString("propose_set_product_subheading_description_and_publish");
    }

    public String proposePublishProduct() {
        return messages.getString("propose_publish_product");
    }

    public String proposeSetProductSubheadingAndPublish() {
        return messages.getString("propose_set_product_subheading_and_publish");
    }

    public String proposeSetProductDescriptionAndPublish() {
        return messages.getString("propose_set_product_description_and_publish");
    }

    public String greeting() {
        return messages.getString("greeting");
    }

    public String userHomeDefaultMessage() {
        return messages.getString("user.home.default_message");
    }

    public String userHomeNoProductsMessage() {
        return messages.getString("user.home.no_products_message");
    }

    public String userHelp(Command homeCommand, Command helpCommand,
                           Command skipCommand, Command cancelCommand) {
        Object[] args = {homeCommand.getUri(), helpCommand.getUri(), skipCommand.getUri(), cancelCommand.getUri()};
        return userHelpFormat.format(args);
    }

    public String userUnknownCommand(Command helpCommand) {
        Object[] args = {helpCommand.getUri()};
        return userUnknownCommandFormat.format(args);
    }

    public String unregisteredUserContactInquiry() {
        return messages.getString("unregistered_user.contact_inquiry");
    }

    public String unregisteredUserHelp() {
        return messages.getString("unregistered_user.help");
    }

    public String askToRegister() {
        return messages.getString("ask_to_register");
    }

    public String registrationSuccessful() {
        return messages.getString("registration_successful");
    }

    public String dashboardDefaultMessage(double todayRevenue, int todayOrdersCount,
                                          double yesterdayRevenue, int yesterdayOrdersCount,
                                          double lastSevenDaysRevenue, int lastSevenDaysOrdersCount) {
        Object[] args = {todayRevenue, todayOrdersCount,
                yesterdayRevenue, yesterdayOrdersCount,
                lastSevenDaysRevenue, lastSevenDaysOrdersCount};

        return dashboardDefaultMessageFormat.format(args);
    }

    public String menuCreationIsCancelled(Command helpCommand) {
        Object[] args = {helpCommand.getUri()};
        return menuCreationIsCancelledFormat.format(args);
    }

    public String administrationAllowed() {
        return messages.getString("administration_allowed");
    }

    public String dashboardNoProductsMessage() {
        return messages.getString("admin.dashboard.no_products_message");
    }

    public String dashboardNoPublishedProductsMessage() {
        return messages.getString("admin.dashboard.no_published_products_message");
    }

    public String nothingToCancel() {
        return messages.getString("nothing_to_cancel");
    }

    public String nothingToSkip() {
        return messages.getString("nothing_to_skip");
    }

    public String askCommandUnknownMessage() {
        return messages.getString("ask_command_unknown_message");
    }

    public String inquireTextMessage() {
        return messages.getString("inquire_text_message");
    }

    public String doubleValueFormatError() {
        return messages.getString("ask_double_value_format_error");
    }

    public String inquireTextMessageForLocality() {
        return messages.getString("ask_locality.inquire_text_message");
    }

    public String localityResolveError() {
        return messages.getString("ask_locality.resolve_error");
    }

    public String inquirePhotoMessage() {
        return messages.getString("inquire_photo_message");
    }

    public String gettingPhotoFilepathError() {
        return messages.getString("ask_photo_get_file_path_error");
    }
}
