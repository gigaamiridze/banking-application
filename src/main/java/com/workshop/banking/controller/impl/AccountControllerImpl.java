package com.workshop.banking.controller.impl;

import com.workshop.banking.controller.AccountController;
import com.workshop.banking.dto.AccountDto;
import com.workshop.banking.model.BankingResponse;
import com.workshop.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AccountControllerImpl implements AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountControllerImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public ResponseEntity<BankingResponse> createAccount(AccountDto accountDto) {
        return accountService.createAccount(accountDto);
    }

    @Override
    public ResponseEntity<BankingResponse> getAccountById(long accountId) {
        return accountService.getAccountById(accountId);
    }

    @Override
    public ResponseEntity<BankingResponse> getAccounts(int pageNumber, int pageSize) {
        return accountService.getAccounts(pageNumber, pageSize);
    }

    @Override
    public ResponseEntity<BankingResponse> depositBalance(long accountId, Map<String, Double> request) {
        Double amount = request.get("amount");
        return accountService.depositBalance(accountId, amount);
    }

    @Override
    public ResponseEntity<BankingResponse> withdrawBalance(long accountId, Map<String, Double> request) {
        Double amount = request.get("amount");
        return accountService.withdrawBalance(accountId, amount);
    }

    @Override
    public ResponseEntity<BankingResponse> deleteAccountById(long accountId) {
        return accountService.deleteAccountById(accountId);
    }
}
