package org.example.service.impl;

import org.example.service.ButtonService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
@Service
public class ButtonServiceImpl implements ButtonService {

    public synchronized void setButtons(SendMessage sendMessage, long chatId){
        sendMessage.setText("Привет! Я бот который поможет тебе отследить твои доходы!");
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Добавить доход");
        keyboard.add(row1);
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Доход за сегодня");
        row2.add("Удалить доход");
        keyboard.add(row2);
        KeyboardRow row3 = new KeyboardRow();
        row3.add("Посмотреть зарплату");
        row3.add("Посмотреть аванс");
        keyboard.add(row3);
        KeyboardRow row4 = new KeyboardRow();
        keyboard.add(row4);
        row4.add("Посмотреть месячную зарплату");
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }
}
