package org.example.model;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 @author Anton Kuchinsky
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="salary")
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private long chatId;
    private double salary;
    private LocalDate date;

}
