package org.example.repository;

import jakarta.transaction.Transactional;
import org.example.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
/**
 @author Anton Kuchinsky
 */

public interface IncomeRepository extends JpaRepository<Income,Long> {
    List<Income> findByChatIdAndDate(long chatId, LocalDate date);

    @Query("SELECT i.income FROM income i WHERE i.chatId = :chatId AND i.date = :date")
    double getIncomeByChatId (@Param("chatId") long chatId, @Param("date") LocalDate date);
    @Modifying
    @Transactional
    @Query("DELETE FROM income i WHERE i.chatId = :chatId AND i.date= :date")
    void deleteIncomeByChatId ( @Param("chatId") long chatId, @Param("date") LocalDate date);
}
