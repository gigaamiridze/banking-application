package com.workshop.banking.controller;

import com.workshop.banking.dto.AccountDto;
import com.workshop.banking.entity.Account;
import com.workshop.banking.model.PageableResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@RequestMapping("/api/v1/bank/account")
public interface AccountController {

    @PostMapping("/create")
    ResponseEntity<AccountDto> createAccount(@Valid @RequestBody AccountDto accountDto);

    @GetMapping("/{accountId}")
    ResponseEntity<AccountDto> getAccountById(@PathVariable long accountId);

    @GetMapping
    ResponseEntity<PageableResponse<Account>> getAccounts(
            @RequestParam(value = "pageNumber", defaultValue = "", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "", required = false) int pageSize
    );

    @PutMapping("/{accountId}/deposit")
    ResponseEntity<AccountDto> depositBalance(@PathVariable long accountId, @RequestBody Map<String, Double> request);

    @PutMapping("/{accountId}/withdraw")
    ResponseEntity<AccountDto> withdrawBalance(@PathVariable long accountId, @RequestBody Map<String, Double> request);

    @DeleteMapping("/{accountId}")
    ResponseEntity<Void> deleteAccountById(@PathVariable long accountId);

    @DeleteMapping("/delete-all")
    ResponseEntity<Void> deleteAllAccount();
}
