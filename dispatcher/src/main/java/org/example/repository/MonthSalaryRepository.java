package org.example.repository;

import org.example.model.MonthSalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
/**
 @author Anton Kuchinsky
 */

public interface MonthSalaryRepository extends JpaRepository<MonthSalary,Long> {
    @Query("SELECT ROUND(m.monthSalary,2) FROM MonthSalary m WHERE m.chatId = :chatId AND m.year = :year AND m.month = :month")
    double getMonthSalaryByChatId (@Param("chatId") long chatId, @Param("year") int year, @Param("month") int month);
}
