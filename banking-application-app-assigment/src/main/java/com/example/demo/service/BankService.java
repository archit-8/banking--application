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

        Account acc = new Account();
        acc.setName(dto.getName());
        acc.setEmail(dto.getEmail());
        acc.setPhoneNumber(dto.getPhone());
        acc.setPassword(dto.getPassword());
        acc.setBalance(BigDecimal.ZERO);

        return accountRepo.save(acc);
    }

    // ---------------- LOGIN ----------------
    @Transactional(readOnly = true)
    public Account login(LoginDTO dto) {
        Account account = accountRepo.findById(dto.getAccountNo())
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

        Account acc = accountRepo.findById(accNo)
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

        Account acc = accountRepo.findById(accNo)
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
        Account fromAccount = accountRepo.findById(dto.getFromAccNo())
                .orElseThrow(() -> new InvalidAccountException("Sender account not found"));

        // Fetch recipient account
        Account toAccount = accountRepo.findById(dto.getToAccNo())
                .orElseThrow(() -> new InvalidAccountException("Receiver account not found"));

        // Check balance
        if (dto.getAmount().compareTo(fromAccount.getBalance()) > 0) {
            throw new InsufficientAmountException("Insufficient balance in sender's account");
        }

        // Update balances
        fromAccount.setBalance(fromAccount.getBalance().subtract(dto.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(dto.getAmount()));

        // Save accounts
        accountRepo.save(fromAccount);
        accountRepo.save(toAccount);
    }

    public Account getAccountByAccNo(Long accNo) {
        return accountRepo.findById(accNo)
                .orElseThrow(() -> new InvalidAccountException("Account not found"));
    }
}


