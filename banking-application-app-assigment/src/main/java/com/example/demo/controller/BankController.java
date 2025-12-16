package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.Account;
import com.example.demo.service.BankService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/atm")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    // ---------------- CREATE ACCOUNT ----------------
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("accountCreateDTO", new AccountCreateDTO());
        return "createAccount";
    }

    @PostMapping("/create")
    public String createAccount(@ModelAttribute AccountCreateDTO dto, Model model) {
        Account account = bankService.createAccount(dto);
        model.addAttribute("account", account);
        return "accountCreated";
    }

    // ---------------- LOGIN ----------------
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO dto, Model model,HttpSession session) {
        Account account = bankService.login(dto);
        
        session.setAttribute("loggedInAccount", account);
        model.addAttribute("account", account);
        return "accountDashboard";
    }

    @GetMapping("/deposit")
    public String depositForm(Model model, @SessionAttribute("loggedInAccount") Account account) {
        model.addAttribute("transactionDTO", new TransactionDTO());
        model.addAttribute("account", account); // for displaying account info
        return "deposit";
    }

    @PostMapping("/deposit")
    public String deposit(@ModelAttribute TransactionDTO dto,
                          Model model,
                          @SessionAttribute("loggedInAccount") Account account) {
        // Always use session account
        Account updatedAccount = bankService.deposit(account.getAccNo(), dto.getAmount());

        model.addAttribute("account", updatedAccount);
        model.addAttribute("message", "Deposit successful!");
        return "transactionSuccess";
    }


    @GetMapping("/withdraw")
    public String withdrawForm(Model model, @SessionAttribute("loggedInAccount") Account account) {
        model.addAttribute("transactionDTO", new TransactionDTO());
        model.addAttribute("account", account);
        return "withdraw";
    }

    @PostMapping("/withdraw")
    public String withdraw(@ModelAttribute TransactionDTO dto,
                           Model model,
                           @SessionAttribute("loggedInAccount") Account account) {
        Account updatedAccount = bankService.withdraw(account.getAccNo(), dto.getAmount());

        model.addAttribute("account", updatedAccount);
        model.addAttribute("message", "Withdrawal successful!");
        System.out.println("print amount wthidrawn");
        return "transactionSuccess";
    }



 // --- TRANSFER ---
    @GetMapping("/transfer")
    public String transferForm(Model model, @SessionAttribute("loggedInAccount") Account account) {
        model.addAttribute("transferDTO", new TransferDTO());
        model.addAttribute("account", account); // needed to know who is sending
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@ModelAttribute TransferDTO dto,
                           Model model,
                           @SessionAttribute("loggedInAccount") Account account) {

        // Set sender account from session
        dto.setFromAccNo(account.getAccNo());

        // Perform transfer
        bankService.transfer(dto);

        // Get updated account info from DB
        Account updatedAccount = bankService.getAccountByAccNo(account.getAccNo());

        model.addAttribute("account", updatedAccount); // THIS FIXES THE THYMELEAF ERROR
        model.addAttribute("message", "Transfer successful!");
        return "transactionSuccess";
    }



}
