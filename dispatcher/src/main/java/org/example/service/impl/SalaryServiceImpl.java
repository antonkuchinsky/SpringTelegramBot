package org.example.service.impl;

import org.example.model.Advance;
import org.example.model.Income;
import org.example.model.MonthSalary;
import org.example.model.Salary;
import org.example.repository.AdvanceRepository;
import org.example.repository.IncomeRepository;
import org.example.repository.MonthSalaryRepository;
import org.example.repository.SalaryRepository;
import org.example.service.SalaryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SalaryServiceImpl implements SalaryService {
    private final AdvanceRepository advanceRepository;
    private final IncomeRepository incomeRepository;
    private final MonthSalaryRepository monthSalaryRepository;
    private final SalaryRepository salaryRepository;

    public SalaryServiceImpl(AdvanceRepository advanceRepository, IncomeRepository incomeRepository, MonthSalaryRepository monthSalaryRepository, SalaryRepository salaryRepository) {
        this.advanceRepository = advanceRepository;
        this.incomeRepository = incomeRepository;
        this.monthSalaryRepository = monthSalaryRepository;
        this.salaryRepository = salaryRepository;
    }

    public void addIncome(Long chatId,double income, LocalDate date){
        var userIncome=Income.builder()
                .income(income)
                .chatId(chatId)
                .date(date)
                .build();
        incomeRepository.save(userIncome);
    }
    public void addSalary(Long chatId,double salary, LocalDate date){
        var userIncome=Salary.builder()
                .salary(salary)
                .chatId(chatId)
                .date(date)
                .build();
        salaryRepository.save(userIncome);
    }
    public void addAdvance(Long chatId,double advance, LocalDate date){
        var userIncome=Advance.builder()
                .advance(advance)
                .chatId(chatId)
                .date(date)
                .build();
        advanceRepository.save(userIncome);
    }
    public void addMonthSalary(Long chatId, double monthSalary, int month, int year){
        var userMonthSalary=MonthSalary.builder()
                .monthSalary(monthSalary)
                .chatId(chatId)
                .month(month)
                .year(year)
                .build();
        monthSalaryRepository.save(userMonthSalary);

    }

    @Override
    public double getSalaryByChatId(long chatId, int year, int month) {
        return salaryRepository.getSalaryByChatId(chatId,year,month);
    }

    @Override
    public void deleteSalaryByChatId(long chatId, LocalDate date) {
        salaryRepository.deleteSalaryByChatId(chatId,date);
    }

    @Override
    public double getMonthSalaryByChatId(long chatId, int year, int month) {
        return monthSalaryRepository.getMonthSalaryByChatId(chatId,year,month);
    }

    @Override
    public double getIncomeByChatId(long chatId, LocalDate date) {
        return incomeRepository.getIncomeByChatId(chatId,date);
    }

    @Override
    public void deleteIncomeByChatId(long chatId, LocalDate date) {
    incomeRepository.deleteIncomeByChatId(chatId,date);
    }

    @Override
    public List<Income> findByChatIdAndDate(long chatId, LocalDate date) {
        return incomeRepository.findByChatIdAndDate(chatId,date);
    }

    @Override
    public double getAdvanceByChatId(long chatId, int year, int month) {
        return advanceRepository.getAdvanceByChatId(chatId,year,month);
    }

    @Override
    public void deleteAdvanceByChatId(long chatId, LocalDate date) {
      advanceRepository.deleteAdvanceByChatId(chatId,date);
    }
}
