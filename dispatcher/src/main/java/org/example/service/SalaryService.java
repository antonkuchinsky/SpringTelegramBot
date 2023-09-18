package org.example.service;

import org.example.model.Income;

import java.time.LocalDate;
import java.util.List;

public interface SalaryService {
    void addIncome(Long chatId,double income, LocalDate date);
    void addSalary(Long chatId,double salary, LocalDate date);
    void addAdvance(Long chatId,double advance, LocalDate date);
    void addMonthSalary(Long chatId, double monthSalary, int month, int year);
    double getSalaryByChatId(long chatId, int year, int month);
    void deleteSalaryByChatId (long chatId,LocalDate date);
    double getMonthSalaryByChatId (long chatId,int year, int month);
    double getIncomeByChatId (long chatId, LocalDate date);
    void deleteIncomeByChatId (long chatId, LocalDate date);
    List<Income> findByChatIdAndDate(long chatId, LocalDate date);
    double getAdvanceByChatId (long chatId, int year, int month);
    void deleteAdvanceByChatId (long chatId, LocalDate date);
}
