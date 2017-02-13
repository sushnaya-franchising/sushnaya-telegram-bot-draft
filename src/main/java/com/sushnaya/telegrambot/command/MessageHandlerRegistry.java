package com.sushnaya.telegrambot.command;

import com.sushnaya.telegrambot.SushnayaBot;

public class MessageHandlerRegistry {
    /*
    admin commands
        /newmoderator
        /moderators

        /newbrand
        /brands

        /newmenu
        /editmenu

        /newpromotion
        /promotions

Меню Разделы Блюда Токен Зоны доставки Акции Статистика Рассылка
У меню есть город

state machine

2 режима: Поиск и не поиск
при поиске paging если несколько продуктов надейно или по городу и т д

при не поиске показывать меню нижнее > Модераторы, Брэнды, Меню* Разделы* Блюда*
     */

    private final SushnayaBot bot;

    public MessageHandlerRegistry(SushnayaBot bot) {
        this.bot = bot;
    }


}
