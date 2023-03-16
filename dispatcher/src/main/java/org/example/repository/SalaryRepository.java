package org.example.repository;

import jakarta.transaction.Transactional;
import org.example.model.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
/**
 @author Anton Kuchinsky
 */

public interface SalaryRepository extends JpaRepository<Salary,Long> {
    @Query("SELECT ROUND(SUM(s.salary),2) FROM salary s WHERE s.chatId = :chatId AND YEAR(s.date) = :year AND MONTH(s.date) = :month")
    double getSalaryByChatId( @Param("chatId") long chatId, @Param("year") int year, @Param("month") int month);
    @Modifying
    @Transactional
    @Query("DELETE FROM salary s WHERE s.chatId = :chatId AND s.date= :date")
    void deleteSalaryByChatId ( @Param("chatId") long chatId, @Param("date") LocalDate date);
}
