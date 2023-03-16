package org.example.repository;

import jakarta.transaction.Transactional;
import org.example.model.Advance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
/**
 @author Anton Kuchinsky
 */

public interface AdvanceRepository extends JpaRepository<Advance,Long> {
    @Query("SELECT ROUND(SUM(a.advance),2) FROM advance a WHERE a.chatId = :chatId AND YEAR(a.date) = :year AND MONTH(a.date) = :month")
    double getAdvanceByChatId (@Param("chatId") long chatId, @Param("year") int year, @Param("month") int month);
    @Modifying
    @Transactional
    @Query("DELETE FROM advance a WHERE a.chatId = :chatId AND a.date= :date")
    void deleteAdvanceByChatId ( @Param("chatId") long chatId, @Param("date") LocalDate date);
}
