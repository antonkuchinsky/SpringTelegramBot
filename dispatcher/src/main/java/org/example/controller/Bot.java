package org.example.controller;

import lombok.extern.log4j.Log4j;
import org.example.config.BotConfig;
import org.example.model.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 @author Anton Kuchinsky
 */
@Component
@Log4j
public class Bot extends TelegramLongPollingBot {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SalaryRepository salaryRepository;
    @Autowired
    private AdvanceRepository advanceRepository;
    @Autowired
    private IncomeRepository incomeRepository;
    @Autowired
    private MonthSalaryRepository monthSalaryRepository;
    State botState=State.ON_WORK;
final BotConfig CONFIG;
public Bot(BotConfig CONFIG){
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
            } catch (TelegramApiException e) {
                log.error(e);
            }
        }
    }
    private String handleMessage(long userId, Message message) throws TelegramApiException {
        String command = message.getText();
        long chatId = message.getChatId();
        LocalDate today=LocalDate.now();
        if (botState == State.ON_WORK) {
            switch (command) {
                case "/start":
                    registerUser(message);
                    SendMessage startMessage=new SendMessage();
                    startMessage.setChatId(chatId);
                    setButtons(startMessage,chatId);
                    return "Выберите действие:";
                case "Добавить доход":
                    botState=State.WAITING_FOR_MESSAGE;
                    break;
                case "Удалить доход":
                    try {
                        double income = incomeRepository.getIncomeByChatId(chatId, today);
                        if (today.getDayOfMonth() <= 15) {
                            incomeRepository.deleteIncomeByChatId(chatId, today);
                            advanceRepository.deleteAdvanceByChatId(chatId, today);
                            return "Сумма " + (income) + " руб. была успешна удалена из вашего аванса.";
                        } else {
                            incomeRepository.deleteIncomeByChatId(chatId, today);
                            salaryRepository.deleteSalaryByChatId(chatId, today);
                            return "Сумма " + (income) + " руб. была успешна удалена из вашей зарплаты.";
                        }
                    }
                    catch (Exception ex){
                        return "У вас нет дохода что-бы удалить его.";
                    }
                case "Доход за сегодня":
                    try {
                        return "Ваш доход за cегодня: " + incomeRepository.getIncomeByChatId(chatId, today) + " руб.";
                    }
                    catch (Exception ex){
                        return "Ваш доход за cегодня: " +0+ " руб.";
                    }

                case "Посмотреть аванс":
                    try {
                        return "Ваш аванс за месяц: " + advanceRepository.getAdvanceByChatId(chatId, today.getYear(), today.getMonthValue()) + " руб.";
                    }
                    catch (Exception ex){
                        return "Ваш аванс за месяц: " + 0 + " руб.";
                    }
                case "Посмотреть зарплату":
                    try {
                        return "Ваша зарплата за месяц " + salaryRepository.getSalaryByChatId(chatId, today.getYear(), today.getMonthValue()) + " руб.";
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
                if (incomeRepository.findByChatIdAndDate(chatId, today).isEmpty()) {
                    try {
                        double income = Double.parseDouble(message.getText());
                        double tax = income * 0.13;
                        income -= tax;
                        botState = State.ON_WORK;
                        if (today.getDayOfMonth() <= 15) {
                            addIncome(chatId, income, today);
                            addAdvance(chatId, income, today);
                            sendMessage(chatId, "Сумма " + (income) + " руб. была зачислена в ваш аванс.");
                        } else {
                            addIncome(chatId, income, today);
                            addSalary(chatId, income, today);
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
                    return "Ваша зарплата за месяц: " + monthSalaryRepository.getMonthSalaryByChatId(chatId, year, month) + " руб.";
                }
                catch (Exception ex){
                    botState=State.ON_WORK;
                    return "Проверьте данные, у вас нету месячной зарплаты за этот месяц.";
                }
            case WAITING_FOR_ADD_MONTH_SALARY:
                botState=State.ON_WORK;
                if (command.equals("Да")) {
                    if (today.getDayOfMonth() >= 25) {
                        double advance = advanceRepository.getAdvanceByChatId(chatId, today.getYear(), today.getMonthValue());
                        double salary = salaryRepository.getSalaryByChatId(chatId, today.getYear(), today.getMonthValue());
                        addMonthSalary(chatId, advance + salary, today.getMonthValue(), today.getYear());
                        return "Ваша месячная запралата за этот месяц составила:" + monthSalaryRepository.getMonthSalaryByChatId(chatId, today.getYear(), today.getMonthValue()) + " руб.";
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

    public synchronized void setButtons(SendMessage sendMessage,long chatId) throws TelegramApiException {
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
        execute(sendMessage);
    }


    private void sendMessage ( long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        }
        catch (TelegramApiException ex){
            log.error(ex);
        }
    }
    private void registerUser(Message message){
    if(userRepository.findById(message.getChatId()).isEmpty()){
        long chatId=message.getChatId();
        Chat chat=message.getChat();
        User user=new User();
        user.setChatId(chatId);
        user.setName(chat.getUserName());
        userRepository.save(user);
        log.info("Пользователь сохранен: " +user);
    }
    }
    private void addIncome(Long chatId,double income, LocalDate date){
    Income userIncome=new Income();
    userIncome.setIncome(income);
    userIncome.setChatId(chatId);
    userIncome.setDate(date);
    incomeRepository.save(userIncome);
    }
    private void addSalary(Long chatId,double salary, LocalDate date){
        Salary userIncome=new Salary();
        userIncome.setSalary(salary);
        userIncome.setChatId(chatId);
        userIncome.setDate(date);
        salaryRepository.save(userIncome);
    }
    private void addAdvance(Long chatId,double advance, LocalDate date){
        Advance userIncome=new Advance();
        userIncome.setAdvance(advance);
        userIncome.setChatId(chatId);
        userIncome.setDate(date);
        advanceRepository.save(userIncome);
    }
    private void addMonthSalary(Long chatId, double monthSalary, int month, int year){
        MonthSalary userMonthSalary=new MonthSalary();
        userMonthSalary.setMonthSalary(monthSalary);
        userMonthSalary.setChatId(chatId);
        userMonthSalary.setMonth(month);
        userMonthSalary.setYear(year);
        monthSalaryRepository.save(userMonthSalary);

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
