package com.example.demo.repository;

import com.example.demo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccNo(Long accNo);

   Optional< Account> findByAccNo(Long accNo);

    boolean existsByEmail(String email);
}
