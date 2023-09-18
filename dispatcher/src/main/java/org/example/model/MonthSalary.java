package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

/**
 @author Anton Kuchinsky
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="MonthSalary")
public class MonthSalary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private long chatId;
    private double monthSalary;
    private int month;
    private int year;
}
