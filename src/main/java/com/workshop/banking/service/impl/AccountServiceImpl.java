package com.workshop.banking.service.impl;

import com.workshop.banking.dto.AccountDto;
import com.workshop.banking.entity.Account;
import com.workshop.banking.mapper.AccountMapper;
import com.workshop.banking.model.BankingResponse;
import com.workshop.banking.repository.AccountRepository;
import com.workshop.banking.service.AccountService;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
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
            if (isInvalidAccountData(accountDto)) {
                return ResponseEntity.badRequest().body(new BankingResponse(false, "Invalid account data", null));
            }

            Account account = AccountMapper.mapToEntity(accountDto);
            Account savedAccount = accountRepository.save(account);
            log.info("Account created successfully: {}", savedAccount);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new BankingResponse(true, "Account created successfully", AccountMapper.mapToDto(savedAccount)));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BankingResponse(false, "Internal server error", null));
        }
    }

    @Override
    public ResponseEntity<BankingResponse> getAccountById(long accountId) {
        try {
            Optional<Account> account = accountRepository.findById(accountId);
            if (!account.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new BankingResponse(false, "Account not found with ID: " + accountId, null));
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BankingResponse(true, "Account retrieved successfully", AccountMapper.mapToDto(account.get())));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BankingResponse(true, "Internal server error", null));
        }
    }

    private boolean isInvalidAccountData(AccountDto accountDto) {
        return StringUtils.isEmpty(accountDto.getAccountHolderName()) || accountDto.getBalance() < 0;
    }
}
