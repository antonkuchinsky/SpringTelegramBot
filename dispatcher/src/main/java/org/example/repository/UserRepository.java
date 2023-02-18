package org.example.repository;

import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 @author Anton Kuchinsky
 */

public interface UserRepository extends JpaRepository<User, Long> {

}

