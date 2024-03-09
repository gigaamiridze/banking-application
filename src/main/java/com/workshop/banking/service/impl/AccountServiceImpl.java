package com.workshop.banking.service.impl;

import com.workshop.banking.dto.AccountDto;
import com.workshop.banking.entity.Account;
import com.workshop.banking.mapper.AccountMapper;
import com.workshop.banking.model.BankingResponse;
import com.workshop.banking.repository.AccountRepository;
import com.workshop.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public ResponseEntity<BankingResponse> createAccount(AccountDto accountDto) {
        try {
            Account account = AccountMapper.mapToEntity(accountDto);
            Account savedAccount = accountRepository.save(account);
            return ResponseEntity.ok(new BankingResponse(true, "Account created successfully", AccountMapper.mapToDto(savedAccount)));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BankingResponse(false, "Internal server error", null));
        }
    }
}
