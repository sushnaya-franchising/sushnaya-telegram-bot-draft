package com.sushnaya.telegrambot.command;

import com.sushnaya.telegrambot.SushnayaBot;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public abstract class AnswerCallbackQueryCommand extends Command{
    private Boolean isAnswerSucceeded;

    public AnswerCallbackQueryCommand(SushnayaBot bot) {
        super(bot);
    }

    public void execute(Update update) {
        AnswerCallbackQuery answer = createAnswerCallbackQuery(update);
        answerCallbackQuery(answer);
    }

    private void answerCallbackQuery(AnswerCallbackQuery answer) {
        try {
            isAnswerSucceeded = bot.answerCallbackQuery(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected abstract AnswerCallbackQuery createAnswerCallbackQuery(Update update);
}
