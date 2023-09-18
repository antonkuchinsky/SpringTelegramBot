package org.example.service.impl;

import lombok.extern.log4j.Log4j;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@Log4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void registerUser(Message message) {
        if (userRepository.findById(message.getChatId()).isEmpty()) {
            var user=User.builder()
                    .chatId(message.getChatId())
                    .name(message.getChat().getUserName())
                    .build();
            userRepository.save(user);
            log.info("Пользователь сохранен: " + user);
        }
    }
}
