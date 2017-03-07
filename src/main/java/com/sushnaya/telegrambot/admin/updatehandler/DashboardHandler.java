package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;

public class DashboardHandler extends SushnayaBotUpdateHandler {
    public DashboardHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        String message = getDashboardMessageText(bot);

        if (update.hasCallbackQuery()) {
            bot.edit(update, message, bot.getAdminKeyboardFactory().dashboardMarkup());

        } else {
            bot.say(update, message, bot.getAdminKeyboardFactory().dashboardMarkup());
        }
    }


    private String getDashboardMessageText(SushnayaBot bot) {
        if (!bot.hasProducts()) {
            return MESSAGES.dashboardNoProductsMessage();
        }

        if (!bot.hasPublishedProducts()) {
            return MESSAGES.dashboardNoPublishedProductsMessage();
        }

        // todo: optimize queries count
        return MESSAGES.dashboardDefaultMessage(
                bot.getDataStorage().getTodayRevenue(),
                bot.getDataStorage().getTodayOrdersCount(),
                bot.getDataStorage().getYesterdayRevenue(),
                bot.getDataStorage().getYesterdayOrdersCount(),
                bot.getDataStorage().getLastNDaysRevenue(7),
                bot.getDataStorage().getLastNDaysOrdersCount(7)
        );
    }
}
