package org.example.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ButtonService {
    void setButtons(SendMessage sendMessage, long chatId);
}
