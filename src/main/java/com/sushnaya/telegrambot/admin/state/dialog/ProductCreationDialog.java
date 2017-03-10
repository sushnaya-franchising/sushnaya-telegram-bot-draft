package com.sushnaya.telegrambot.admin.state.dialog;

import com.sushnaya.entity.Product;
import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.admin.keyboard.AdminKeyboardMarkupFactory;
import com.sushnaya.telegrambot.admin.state.*;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.REPLY_KEYBOARD_REMOVE;

public class ProductCreationDialog extends AdminBotDialogState<Product> {
    private static final Function<Update, Command> COMPLETION_STEP_EXTRA_COMMAND_PARSER = u -> {
        if (u.hasMessage() && u.getMessage().hasText()) {
            final String text = u.getMessage().getText();

            if (text.equalsIgnoreCase(MESSAGES.setProductSubheading())) {
                return SET_PRODUCT_SUBHEADING;
            }

            if (text.equalsIgnoreCase(MESSAGES.setProductDescription())) {
                return SET_PRODUCT_DESCRIPTION;
            }

            if (text.equalsIgnoreCase(MESSAGES.skipProductPublication())) {
                return SKIP;
            }

            if (text.equalsIgnoreCase(MESSAGES.publishProduct())) {
                return PUBLISH_PRODUCT;
            }
        }

        return NOP;
    };

    private final AdminKeyboardMarkupFactory keyboardMarkupFactory;
    private final AdminBotDialogState[] steps;
    private final AskTextState nameStep;
    private final AskDoubleValueState priceStep;
    private final AskPhotoState photoStep;
    private final AskTextState subheadingStep;
    private final AskTextState descriptionStep;
    private final AskCommandState completionStep;

    public ProductCreationDialog(SushnayaBot bot) {
        super(bot);

        this.keyboardMarkupFactory = bot.getAdminKeyboardFactory();

        steps = new AdminBotDialogState[]{
                nameStep = (AskTextState) new AskTextState(bot)
                        .setDefaultMessage(MESSAGES.askProductName())
                        .setDefaultKeyboard(REPLY_KEYBOARD_REMOVE),

                priceStep = (AskDoubleValueState) new AskDoubleValueState(bot)
                        .setDefaultMessage(MESSAGES.askProductPrice())
                        .setHelpMessage(MESSAGES.askProductPriceHelp()),

                photoStep = (AskPhotoState) new AskPhotoState(bot)
                        .setDefaultMessage(MESSAGES.askProductPhoto())
                        .setDefaultKeyboard(this.keyboardMarkupFactory.skipProductPhotoStepMarkup())
                        .setSkippable(true),

                subheadingStep = (AskTextState) new AskTextState(bot)
                        .setDefaultMessage(MESSAGES.askProductSubheading())
                        .setHelpMessage(MESSAGES.askProductSubheadingHelp())
                        .setDefaultKeyboard(this.keyboardMarkupFactory.skipProductSubheadingStepMarkup())
                        .setSkippable(true),

                descriptionStep = (AskTextState) new AskTextState(bot)
                        .setDefaultMessage(MESSAGES.askProductDescription())
                        // todo: add help message with markdown support info!
                        .setDefaultKeyboard(this.keyboardMarkupFactory.skipProductDescriptionStepMarkup())
                        .setSkippable(true),

                completionStep = (AskCommandState) new AskCommandState(bot)
                        .setDefaultMessage(MESSAGES.proposeSetProductSubheadingDescriptionAndPublish())
                        // todo: add help message with detailed explanation about publication
                        .setSkippable(true)
                        .setExtraCommandParser(COMPLETION_STEP_EXTRA_COMMAND_PARSER)
        };
    }

    @Override
    public AdminBotDialogState<Product> onCancel(Consumer<Update> onCancel) {
        for (AdminBotDialogState step : steps) {
            step.onCancel(onCancel);
        }

        return super.onCancel(onCancel);
    }

    @Override
    public AdminBotDialogState<Product> ask(Update update, String message, ReplyKeyboard keyboard) {
        Product product = new Product();

        nameStep.ask(update, message != null ? message : MESSAGES.askProductName())
                .then((u, productName) -> {
                    product.setName(productName);

                    final String productDisplayName = product.getDisplayNameWithPrice(MESSAGES.getLocale());

                    priceStep.ask(u, MESSAGES.valueConfirmation(productDisplayName) + " " +
                            MESSAGES.askProductPriceForProductCreation());
                });

        priceStep.then((u, productPrice) -> {
            product.setPrice(productPrice);

            final String productDisplayName = product.getDisplayNameWithPrice(MESSAGES.getLocale());

            photoStep.ask(u, MESSAGES.valueConfirmation(productDisplayName) + " " +
                    MESSAGES.askProductPhotoForProductCreation());
        });

        photoStep.then((u, productPhotoFileId) -> {
            product.setTelegramPhotoFileId(productPhotoFileId);

            completionStep.ask(u, keyboardMarkupFactory.productCreationCompletion(
                    true, true));
        });

        subheadingStep.then((u, productSubheading) -> {
            product.setSubheading(productSubheading);

            askCompletionAgain(u, product);
        });

        descriptionStep.then((u, productDescription) -> {
            product.setDescription(productDescription);
            // todo: support description.md file

            askCompletionAgain(u, product);
        });

        completionStep.then((u, v) -> getThen().accept(u, product))
                .ifThen(SET_PRODUCT_SUBHEADING, subheadingStep::ask)
                .ifThen(SET_PRODUCT_DESCRIPTION, descriptionStep::ask)
                .ifThen(SKIP, (u) -> getThen().accept(u, product))
                .ifThen(PUBLISH_PRODUCT, (u) -> {
                    product.setPublished(true);
                    getThen().accept(u, product);
                });

        return this;
    }

    private void askCompletionAgain(Update u, Product product) {
        String message = getProposalForCompletionStep(product);
        final ReplyKeyboard keyboard = keyboardMarkupFactory.productCreationCompletion(
                !product.hasSubheading(), !product.hasDescription());

        completionStep.ask(u, message, keyboard);
    }

    private String getProposalForCompletionStep(Product product) {
        if (product.hasSubheading() && product.hasDescription()) {
            return MESSAGES.proposePublishProduct();

        } else if (!product.hasSubheading() && product.hasDescription()) {
            return MESSAGES.proposeSetProductSubheadingAndPublish();

        } else if (product.hasSubheading() && !product.hasDescription()) {
            return MESSAGES.proposeSetProductDescriptionAndPublish();
        }

        return completionStep.getDefaultMessage();
    }

    @Override
    protected void handleUpdate(Update update, BiConsumer<Update, Product> ok, BiConsumer<Update, String> ko) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
