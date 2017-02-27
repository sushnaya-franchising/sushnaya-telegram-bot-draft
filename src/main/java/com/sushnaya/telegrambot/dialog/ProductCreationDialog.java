package com.sushnaya.telegrambot.dialog;

import com.sushnaya.entity.Product;
import com.sushnaya.telegrambot.AdminKeyboardMarkupFactory;
import com.sushnaya.telegrambot.Messages;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.state.admin.*;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.KeyboardMarkupFactory.REPLY_KEYBOARD_REMOVE;

public class ProductCreationDialog extends AdminBotDialogState<Product> {
    private static final Messages MESSAGES = Messages.getDefaultMessages();
    private final AdminKeyboardMarkupFactory keyboardMarkupFactory;
    private final AdminBotDialogState[] steps;
    private final AskTextState productNameStep;
    private final AskDoubleValueState productPriceStep;
    private final AskPhotoState productPhotoStep;
    private final AskTextState productSubheadingStep;
    private final AskTextState productDescriptionStep;
    private final AskCommandState productCreationCompletionStep;

    public ProductCreationDialog(SushnayaBot bot, AdminKeyboardMarkupFactory keyboardMarkupFactory) {
        super(bot);

        this.keyboardMarkupFactory = keyboardMarkupFactory;

        steps = new AdminBotDialogState[]{
                productNameStep = (AskTextState) new AskTextState(bot)
                        .setDefaultMessage(MESSAGES.askProductName())
                        .setHelpMessage(MESSAGES.cancelMenuCreationHelp(CANCEL))
                        .setDefaultKeyboard(REPLY_KEYBOARD_REMOVE),

                productPriceStep = (AskDoubleValueState) new AskDoubleValueState(bot)
                        .setDefaultMessage(MESSAGES.askProductPrice())
                        .setHelpMessage(MESSAGES.askProductPriceHelp() + "\n\n" +
                                MESSAGES.cancelMenuCreationHelp(CANCEL)),

                productPhotoStep = (AskPhotoState) new AskPhotoState(bot)
                        .setDefaultMessage(MESSAGES.askProductPhoto())
                        .setHelpMessage(MESSAGES.skipProductPhotoStepHelp(SKIP) + "\n\n" +
                                MESSAGES.cancelMenuCreationHelp(CANCEL))
                        .setDefaultKeyboard(this.keyboardMarkupFactory.skipCategoryPhotoStepMarkup())
                        .setSkippable(true),

                productSubheadingStep = (AskTextState) new AskTextState(bot)
                        .setDefaultMessage(MESSAGES.askProductSubheading())
                        .setHelpMessage((MESSAGES.askProductSubheadingHelp() + "\n\n" +
                                MESSAGES.skipProductSubheadingStepHelp(SKIP) + "\n\n" +
                                MESSAGES.cancelMenuCreationHelp(CANCEL)))
                        .setDefaultKeyboard(this.keyboardMarkupFactory.skipProductSubheadingStepMarkup())
                        .setSkippable(true),

                productDescriptionStep = (AskTextState) new AskTextState(bot)
                        .setDefaultMessage(MESSAGES.askProductDescription())
                        .setHelpMessage(MESSAGES.skipProductDescriptionStepHelp(SKIP) + "\n\n" +
                                MESSAGES.cancelMenuCreationHelp(CANCEL))
                        .setDefaultKeyboard(this.keyboardMarkupFactory.skipProductDescriptionStepMarkup())
                        .setSkippable(true),

                productCreationCompletionStep = (AskCommandState) new AskCommandState(bot)
                        .setDefaultMessage(MESSAGES.proposeSetProductSubheadingDescriptionAndPublish())
                        .setHelpMessage(MESSAGES.cancelMenuCreationHelp(CANCEL))
                        .setDefaultKeyboard(this.keyboardMarkupFactory.productCreationCompletion(
                                true, true))
                        .setSkippable(true)
                        .ifThen(SET_PRODUCT_SUBHEADING, productSubheadingStep::ask)
                        .ifThen(SET_PRODUCT_DESCRIPTION, productDescriptionStep::ask)
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

        productNameStep.ask(update, message != null ? message : MESSAGES.askProductName())
                .then((u, productName) -> {
                    product.setName(productName);

                    final String productDisplayName = product.getDisplayName(MESSAGES.getLocale());

                    productPriceStep.ask(u, MESSAGES.valueConfirmation(productDisplayName) + " " +
                            MESSAGES.askProductPriceForProductCreation());
                });

        productPriceStep.then((u, productPrice) -> {
            product.setPrice(productPrice);

            final String productDisplayName = product.getDisplayName(MESSAGES.getLocale());

            productPhotoStep.ask(u, MESSAGES.valueConfirmation(productDisplayName) + " " +
                    MESSAGES.askProductPhotoForProductCreation());
        });

        productPhotoStep.then((u, productPhotoFilePath) -> {
            product.setTelegramFilePath(productPhotoFilePath);

            productCreationCompletionStep.ask(u);
        });

        productSubheadingStep.then((u, productSubheading) -> {
            product.setSubheading(productSubheading);

            askProductCreationCompletionAgain(u, product);
        });

        productDescriptionStep.then((u, productDescription) -> {
            product.setDescription(productDescription);
            // todo: support description.md file

            askProductCreationCompletionAgain(u, product);
        });

        productCreationCompletionStep.then((u, v) -> getThen().accept(u, product))
                .ifThen(SKIP_PRODUCT_PUBLICATION, (u) -> getThen().accept(u, product))
                .ifThen(PUBLISH_PRODUCT, (u) -> {
                    product.setPublished(true);
                    getThen().accept(u, product);
                });

        return this;
    }

    private void askProductCreationCompletionAgain(Update u, Product product) {
        final ReplyKeyboard replyKeyboard = keyboardMarkupFactory.productCreationCompletion(
                !product.hasSubheading(), !product.hasDescription());
        productCreationCompletionStep.setDefaultKeyboard(replyKeyboard);

        if (product.hasSubheading() && product.hasDescription()) {
            productCreationCompletionStep.setDefaultMessage(MESSAGES.proposePublishProduct());

        } else if (!product.hasSubheading() && product.hasDescription()) {
            productCreationCompletionStep.setDefaultMessage(MESSAGES.proposeSetProductSubheadingAndPublish());

        } else if (product.hasSubheading() && !product.hasDescription()) {
            productCreationCompletionStep.setDefaultMessage(MESSAGES.proposeSetProductDescriptionAndPublish());
        }

        productCreationCompletionStep.ask(u);
    }

    @Override
    protected void handleUpdate(Update update, BiConsumer<Update, Product> ok, BiConsumer<Update, String> ko) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
