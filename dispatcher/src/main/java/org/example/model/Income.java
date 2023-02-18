package org.example.model;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
/**
 @author Anton Kuchinsky
 */
@Getter
@Setter
@Entity(name = "income")
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private long chatId;
    private double income;
    private LocalDate date;

}
