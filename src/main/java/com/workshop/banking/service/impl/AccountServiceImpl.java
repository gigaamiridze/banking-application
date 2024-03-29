package com.workshop.banking.service.impl;

import com.workshop.banking.dto.AccountDto;
import com.workshop.banking.entity.Account;
import com.workshop.banking.mapper.AccountMapper;
import com.workshop.banking.model.BankingResponse;
import com.workshop.banking.model.PageableResponse;
import com.workshop.banking.repository.AccountRepository;
import com.workshop.banking.service.AccountService;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                    .body(new BankingResponse(false, "Internal server error", null));
        }
    }

    @Override
    public ResponseEntity<BankingResponse> getAccounts(int pageNumber, int pageSize) {
        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Account> accounts = accountRepository.findAll(pageable);
            List<Account> listOfAccount = accounts.getContent();
            List<AccountDto> content = listOfAccount.stream().map(AccountMapper::mapToDto).collect(Collectors.toList());

            PageableResponse<AccountDto> pageableResponse = new PageableResponse<>();
            pageableResponse.setContent(content);
            pageableResponse.setPageNumber(accounts.getNumber());
            pageableResponse.setPageSize(accounts.getSize());
            pageableResponse.setTotalPages(accounts.getTotalPages());
            pageableResponse.setTotalElements(accounts.getTotalElements());
            pageableResponse.setLast(accounts.isLast());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BankingResponse(true, "Accounts retrieved successfully", pageableResponse));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BankingResponse(false, "Internal server error", null));
        }
    }

    @Override
    public ResponseEntity<BankingResponse> depositBalance(long accountId, double amount) {
        try {
            Optional<Account> accountOptional = accountRepository.findById(accountId);
            if (!accountOptional.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new BankingResponse(false, "Account not found with ID: " + accountId, null));
            }

            Account account = accountOptional.get();
            double balance = account.getBalance() + amount;
            account.setBalance(balance);
            Account savedAccount = accountRepository.save(account);
            log.info("Account balance deposited successfully for {}. Current balance {}", savedAccount.getAccountHolderName(),
                    savedAccount.getBalance());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BankingResponse(true, "Balance deposited successfully", AccountMapper.mapToDto(savedAccount)));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BankingResponse(false, "Internal server error", null));
        }
    }

    @Override
    public ResponseEntity<BankingResponse> withdrawBalance(long accountId, double amount) {
        try {
            Optional<Account> accountOptional = accountRepository.findById(accountId);
            if (!accountOptional.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new BankingResponse(false, "Account not found with ID: " + accountId, null));
            }

            Account account = accountOptional.get();
            if (account.getBalance() < amount) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(new BankingResponse(false, "Insufficient amount: " + account.getBalance(), null));
            }

            double totalBalance = account.getBalance() - amount;
            account.setBalance(totalBalance);
            Account savedAccount = accountRepository.save(account);
            log.info("Account balance withdraw successfully for {}. Current balance {}", savedAccount.getAccountHolderName(),
                    savedAccount.getBalance());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BankingResponse(true, "Balance withdraw successfully", AccountMapper.mapToDto(savedAccount)));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BankingResponse(false, "Internal server error", null));
        }
    }

    @Override
    public ResponseEntity<BankingResponse> deleteAccountById(long accountId) {
        try {
            Optional<Account> accountOptional = accountRepository.findById(accountId);
            if (!accountOptional.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new BankingResponse(false, "Account not found with ID: " + accountId, null));
            }

            accountRepository.deleteById(accountId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BankingResponse(true, "Account deleted successfully", AccountMapper.mapToDto(accountOptional.get())));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BankingResponse(false, "Internal server error", null));
        }
    }

    @Override
    public ResponseEntity<BankingResponse> deleteAllAccount() {
        try {
            List<Account> accounts = accountRepository.findAll();
            if (accounts.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body(new BankingResponse(true, "No accounts found to delete", null));
            }

            accountRepository.deleteAll();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BankingResponse(true, "All accounts deleted successfully", null));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BankingResponse(false, "Internal server error", null));
        }
    }

    private boolean isInvalidAccountData(AccountDto accountDto) {
        return StringUtils.isEmpty(accountDto.getAccountHolderName()) || accountDto.getBalance() < 0;
    }
}
