package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
/**
 @author Anton Kuchinsky
 */
@Getter
@Setter
@Entity(name="userstable")
public class User {
    @Id
    private long chatId;
    private String name;

    @Override
    public String toString() {
        return "User " + "chatId=" + chatId + " name= "+ name;
    }
}
