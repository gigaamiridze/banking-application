package com.workshop.banking.controller;

import com.workshop.banking.dto.AccountDto;
import com.workshop.banking.model.BankingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/bank/account")
public interface AccountController {

    @PostMapping("/create")
    ResponseEntity<BankingResponse> createAccount(@RequestBody AccountDto accountDto);

    @GetMapping("/{accountId}")
    ResponseEntity<BankingResponse> getAccountById(@PathVariable long accountId);
}
