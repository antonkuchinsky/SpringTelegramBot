package org.example.model;

import jakarta.persistence.*;
import lombok.*;

/**
 @author Anton Kuchinsky
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="users")
public class User {
    @Id
    private long chatId;
    private String name;
    @Override
    public String toString() {
        return "User " + "chatId=" + chatId + " name= "+ name;
    }
}
