package com.example.demo.service;

import com.example.demo.dto.AccountCreateDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.TransferDTO;
import com.example.demo.exception.InsufficientAmountException;
import com.example.demo.exception.InvalidAccountException;
import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BankService {

    private final AccountRepository accountRepo;

    public BankService(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    // ---------------- CREATE ACCOUNT ----------------

    @Transactional
    public Account createAccount(AccountCreateDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("Account data is required");
        }
        if (accountRepo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        Account acc = new Account();
        acc.setName(dto.getName());
        acc.setEmail(dto.getEmail());
        acc.setPhoneNumber(dto.getPhone());
        acc.setPassword(validateAndEncodePassword(dto.getPassword()));
        acc.setBalance(BigDecimal.ZERO);
        acc.setAccNo(generateUniqueAccountNumber());
        return accountRepo.save(acc);
    }


    // ---------------- LOGIN ----------------
    @Transactional(readOnly = true)
    public Account login(LoginDTO dto) {
        Account account = accountRepo.findByAccNo(dto.getAccountNo())
                .orElseThrow(() -> new InvalidAccountException("Account not found"));

        if (!account.getPassword().equals(dto.getPassword())) {
            throw new InvalidAccountException("Invalid password");
        }

        return account;
    }

    // ---------------- DEPOSIT ----------------
    @Transactional
    public Account deposit(Long accNo, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Account acc = accountRepo.findByAccNo(accNo)
                .orElseThrow(() -> new InvalidAccountException("Account not found"));

        acc.setBalance(acc.getBalance().add(amount));
        return accountRepo.save(acc);
    }

    // ---------------- WITHDRAW ----------------
    @Transactional
    public Account withdraw(Long accNo, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Account acc = accountRepo.findByAccNo(accNo)
                .orElseThrow(() -> new InvalidAccountException("Account not found"));

        if (amount.compareTo(acc.getBalance()) > 0) {
            throw new InsufficientAmountException("Insufficient balance");
        }

        acc.setBalance(acc.getBalance().subtract(amount));
        return accountRepo.save(acc);
    }

    @Transactional
    public void transfer(TransferDTO dto) {
        // Fetch sender account
        Account fromAccount = accountRepo.findByAccNo(dto.getFromAccNo())
                .orElseThrow(() -> new InvalidAccountException("Sender account not found"));

        // Fetch recipient account
        Account toAccount = accountRepo.findByAccNo(dto.getToAccNo())
                .orElseThrow(() -> new InvalidAccountException("Receiver account not found"));

        if (dto.getAmount().compareTo(fromAccount.getBalance()) > 0) {
            throw new InsufficientAmountException("Insufficient balance in sender's account");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(dto.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(dto.getAmount()));

        accountRepo.save(fromAccount);
        accountRepo.save(toAccount);
    }

    public Account getAccountByAccNo(Long accNo) {
        return accountRepo.findByAccNo(accNo)
                .orElseThrow(() -> new InvalidAccountException("Account not found"));
    }
    private Long generateUniqueAccountNumber() {
        Long accNo;
        do {
            accNo = ThreadLocalRandom.current()
                    .nextLong(10_000_000_000L, 100_000_000_000L);
        } while (accountRepo.existsByAccNo(accNo));
        return accNo;
    }
    private String validateAndEncodePassword(String password) {

        if (password == null) {
            throw new RuntimeException("Password cannot be null");
        }

        String regex =
                "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$";

        if (!password.matches(regex)) {
            throw new RuntimeException(
                    "Password must be at least 8 characters long and contain uppercase, lowercase, digit, and special character"
            );
        }

        // Later you can add BCrypt here
        return password;
    }



}


