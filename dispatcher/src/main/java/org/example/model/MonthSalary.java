package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
/**
 @author Anton Kuchinsky
 */

@Getter
@Setter
@Entity(name="MonthSalary")
public class MonthSalary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private long chatId;
    private double monthSalary;
    private int month;
    private int year;
}
