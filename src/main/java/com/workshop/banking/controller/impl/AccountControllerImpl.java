package com.workshop.banking.controller.impl;

import com.workshop.banking.controller.AccountController;
import com.workshop.banking.dto.AccountDto;
import com.workshop.banking.entity.Account;
import com.workshop.banking.mapper.AccountMapper;
import com.workshop.banking.model.PageableResponse;
import com.workshop.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AccountControllerImpl implements AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public ResponseEntity<AccountDto> createAccount(AccountDto accountDto) {
        Account account = accountMapper.mapToEntity(accountDto);
        Account accountResponse = accountService.createAccount(account);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountMapper.mapToDto(accountResponse));
    }

    @Override
    public ResponseEntity<AccountDto> getAccountById(long accountId) {
        Account account = accountService.getAccountById(accountId);
        return ResponseEntity.ok(accountMapper.mapToDto(account));
    }

    @Override
    public ResponseEntity<PageableResponse<Account>> getAccounts(int pageNumber, int pageSize) {
        PageableResponse<Account> pageableResponse = accountService.getAccounts(pageNumber, pageSize);
        return ResponseEntity.ok(pageableResponse);
    }

    @Override
    public ResponseEntity<AccountDto> depositBalance(long accountId, Map<String, Double> request) {
        Double amount = request.get("amount");
        Account account = accountService.depositBalance(accountId, amount);
        return ResponseEntity.ok(accountMapper.mapToDto(account));
    }

    @Override
    public ResponseEntity<AccountDto> withdrawBalance(long accountId, Map<String, Double> request) {
        Double amount = request.get("amount");
        Account account = accountService.withdrawBalance(accountId, amount);
        return ResponseEntity.ok(accountMapper.mapToDto(account));
    }

    @Override
    public ResponseEntity<Void> deleteAccountById(long accountId) {
        accountService.deleteAccountById(accountId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteAllAccount() {
        accountService.deleteAllAccount();
        return ResponseEntity.ok().build();
    }
}
