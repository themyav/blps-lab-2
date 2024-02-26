package com.blps.lab1.repo;

import com.blps.lab1.entity.Balance;
import com.blps.lab1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long> {
}