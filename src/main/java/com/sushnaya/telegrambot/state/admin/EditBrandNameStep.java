package com.sushnaya.telegrambot.state.admin;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.Command;
import com.sushnaya.telegrambot.command.admin.RevealBrandsManagement;
import com.sushnaya.telegrambot.command.admin.SaveBrandCommand;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.command.Command.Uri.CANCEL_URI;
import static com.sushnaya.telegrambot.command.Command.Uri.parseCommandUri;

public class EditBrandNameStep extends AdminOnDefaultState {

    private final Message questionMessage;
    private SaveBrandCommand saveBrandCommand = new SaveBrandCommand(bot);
    private RevealBrandsManagement revealBrandsManagement = new RevealBrandsManagement(bot);

    public EditBrandNameStep(SushnayaBot bot, Message questionMessage) {
        super(bot);

        if (questionMessage == null) {
            throw new IllegalArgumentException("questionMessage must be not null");
        }

        this.questionMessage = questionMessage;
    }

    protected Command getCommand(Update update) {
        if (update.hasMessage() && isReplyToQuestion(update.getMessage())) {
            return saveBrandCommand;
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (parseCommandUri(update.getMessage().getText())) {
                case CANCEL_URI:
                    return revealBrandsManagement;
            }
        }

        return super.getCommand(update);
    }

    private boolean isReplyToQuestion(Message message) {
        return message.isReply() &&
                message.getReplyToMessage().getMessageId().equals(
                        questionMessage.getMessageId());
    }
}
