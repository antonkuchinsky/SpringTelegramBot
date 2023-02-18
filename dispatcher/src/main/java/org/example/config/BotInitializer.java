package org.example.config;

import org.example.controller.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
/**
 @author Anton Kuchinsky
 */
@Component

public class BotInitializer {
    @Autowired
    Bot bot;
    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi=new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);

    }
}