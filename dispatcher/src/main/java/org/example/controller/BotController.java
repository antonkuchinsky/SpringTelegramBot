package org.example.controller;

import lombok.extern.log4j.Log4j;
import org.example.config.BotConfig;
import org.example.model.State;
import org.example.service.ButtonService;
import org.example.service.SalaryService;
import org.example.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;

/**
 @author Anton Kuchinsky
 */
@Component
@Log4j
public class BotController extends TelegramLongPollingBot {
    private final SalaryService salaryService;
    private final ButtonService buttonService;
    private final UserService userService;
    private final BotConfig CONFIG;
    State botState=State.ON_WORK;

public BotController(SalaryService salaryService, ButtonService buttonService, UserService userService, BotConfig CONFIG){
    this.salaryService = salaryService;
    this.buttonService = buttonService;
    this.userService = userService;
    this.CONFIG=CONFIG;
}
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            int userId = message.getFrom().getId().intValue();
            try {
                String response = handleMessage(userId, message);
                sendMessage(message.getChatId(), response);
            }
            catch (TelegramApiException ex){
                log.error(ex);
            }
        }
        }
    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try{
            execute(message);
        }
        catch (TelegramApiException ex){
            log.error(ex);
        }
    }
    public String handleMessage(long userId, Message message) throws TelegramApiException {
        String command = message.getText();
        long chatId = message.getChatId();
        LocalDate today=LocalDate.now();

        if (botState == State.ON_WORK) {
            switch (command) {
                case "/start":
                    userService.registerUser(message);
                    SendMessage startMessage=new SendMessage();
                    startMessage.setChatId(chatId);
                    buttonService.setButtons(startMessage,chatId);
                    return "Выберите действие:";

                case "Добавить доход":
                    botState=State.WAITING_FOR_MESSAGE;
                    break;

                case "Удалить доход":
                    try {
                        double income = salaryService.getIncomeByChatId(chatId, today);
                        if (today.getDayOfMonth() <= 15) {
                            salaryService.deleteIncomeByChatId(chatId, today);
                            salaryService.deleteAdvanceByChatId(chatId, today);
                            return "Сумма " + (income) + " руб. была успешна удалена из вашего аванса.";
                        } else {
                            salaryService.deleteIncomeByChatId(chatId, today);
                            salaryService.deleteSalaryByChatId(chatId, today);
                            return "Сумма " + (income) + " руб. была успешна удалена из вашей зарплаты.";
                        }
                    }
                    catch (Exception ex){
                        return "У вас нет дохода что-бы удалить его.";
                    }

                case "Доход за сегодня":
                    try {
                        return "Ваш доход за cегодня: " + salaryService.getIncomeByChatId(chatId, today) + " руб.";
                    }
                    catch (Exception ex){
                        return "Ваш доход за cегодня: " +0+ " руб.";
                    }

                case "Посмотреть аванс":
                    try {
                        return "Ваш аванс за месяц: " + salaryService.getAdvanceByChatId(chatId, today.getYear(), today.getMonthValue()) + " руб.";
                    }
                    catch (Exception ex){
                        return "Ваш аванс за месяц: " + 0 + " руб.";
                    }

                case "Посмотреть зарплату":
                    try {
                        return "Ваша зарплата за месяц " + salaryService.getSalaryByChatId(chatId, today.getYear(), today.getMonthValue()) + " руб.";
                    }
                    catch (Exception ex){
                        return "Ваша зарплата за месяц " + 0 + " руб.";
                    }

                case "Посмотреть месячную зарплату":
                    botState=State.WAITING_FOR_MONTH_SALARY;
                    sendMessage(chatId,"Введите месяц и год за который хотите посмотерть месячную зарплату \nПример: 02-2023");
                    break;
                default:
                    return "Неизвестная команда. Введите /start для получения списка доступных команд.";


            }
            return "Введите значение:";
        }
        switch(botState){
            case WAITING_FOR_MESSAGE:
                if (salaryService.findByChatIdAndDate(chatId, today).isEmpty()) {
                    try {
                        double income = Double.parseDouble(message.getText());
                        double tax = income * 0.13;
                        income -= tax;
                        botState = State.ON_WORK;
                        if (today.getDayOfMonth() <= 15) {
                            salaryService.addIncome(chatId, income, today);
                            salaryService.addAdvance(chatId, income, today);
                            sendMessage(chatId, "Сумма " + (income) + " руб. была зачислена в ваш аванс.");
                        } else {
                            salaryService.addIncome(chatId, income, today);
                            salaryService.addSalary(chatId, income, today);
                            sendMessage(chatId, "Сумма " + (income) + " руб. была зачислена в вашу зарплату.");
                        }
                        if (today.getDayOfMonth() >= 25) {
                            botState = State.WAITING_FOR_ADD_MONTH_SALARY;
                            return "Это ваш последний доход за этот месяц?\nВведите:Да/Нет";
                        }
                        else {
                            return "Доход " + (income) + " руб. был успешно добавлен";
                        }
                    } catch (NumberFormatException e) {
                        return "Неверный формат команды. Пожалуйста введите число.";
                    }
                }
                else{
                    botState=State.ON_WORK;
                    return "Вы уже ввели доход за сегодня.";
                }
            case WAITING_FOR_MONTH_SALARY:
                try {
                    botState=State.ON_WORK;
                    String[] commandParts = command.split("-");
                    int month = Integer.parseInt(commandParts[0]);
                    int year = Integer.parseInt(commandParts[1]);
                    return "Ваша зарплата за месяц: " + salaryService.getMonthSalaryByChatId(chatId, year, month) + " руб.";
                }
                catch (Exception ex){
                    botState=State.ON_WORK;
                    return "Проверьте данные, у вас нету месячной зарплаты за этот месяц.";
                }
            case WAITING_FOR_ADD_MONTH_SALARY:
                botState=State.ON_WORK;
                if (command.equals("Да")) {
                    if (today.getDayOfMonth() >= 25) {
                        double advance = salaryService.getAdvanceByChatId(chatId, today.getYear(), today.getMonthValue());
                        double salary = salaryService.getSalaryByChatId(chatId, today.getYear(), today.getMonthValue());
                        salaryService.addMonthSalary(chatId, advance + salary, today.getMonthValue(), today.getYear());
                        return "Ваша месячная запралата за этот месяц составила:" + salaryService.getMonthSalaryByChatId(chatId, today.getYear(), today.getMonthValue()) + " руб.";
                    }
                }
                if (command.equals("Нет")) {
                    return "Спасибо за ответ";
                }

                else {
                    botState = State.WAITING_FOR_ADD_MONTH_SALARY;
                    return "Вы ввели некорректный ответ.\nПожалуйста введите:Да/Нет";
                }

        }

        return "";
    }

    @Override
    public String getBotUsername() {
        return CONFIG.getBotName();
    }

    @Override
    public String getBotToken() {
        return CONFIG.getToken();
    }

}
