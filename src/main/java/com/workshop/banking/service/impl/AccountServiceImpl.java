package com.workshop.banking.service.impl;

import com.workshop.banking.entity.Account;
import com.workshop.banking.exception.ResourceNotFoundException;
import com.workshop.banking.model.PageableResponse;
import com.workshop.banking.repository.AccountRepository;
import com.workshop.banking.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Account createAccount(Account account) {
        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully: {}", savedAccount);
        return savedAccount;
    }

    @Override
    public Account getAccountById(long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (!account.isPresent()) {
            throw new ResourceNotFoundException("Account not found with ID: " + accountId);
        }
        return account.get();
    }

    @Override
    public PageableResponse<Account> getAccounts(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Account> accounts = accountRepository.findAll(pageable);
        List<Account> content = accounts.getContent();

        PageableResponse<Account> pageableResponse = new PageableResponse<>(
                content,
                accounts.getNumber(),
                accounts.getSize(),
                accounts.getTotalPages(),
                accounts.getTotalElements(),
                accounts.isFirst(),
                accounts.isLast(),
                accounts.isEmpty()
        );

        return pageableResponse;
    }

    @Override
    public Account depositBalance(long accountId, double amount) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (!accountOptional.isPresent()) {
            throw new ResourceNotFoundException("Account not found with ID: " + accountId);
        }

        Account account = accountOptional.get();
        double balance = account.getBalance() + amount;
        account.setBalance(balance);
        Account savedAccount = accountRepository.save(account);
        log.info("Account balance deposited successfully for {}. Current balance {}", savedAccount.getAccountHolderName(),
                savedAccount.getBalance());
        return savedAccount;
    }

    @Override
    public Account withdrawBalance(long accountId, double amount) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (!accountOptional.isPresent()) {
            throw new ResourceNotFoundException("Account not found with ID: " + accountId);
        }

        Account account = accountOptional.get();
        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient amount: " + account.getBalance());
        }

        double totalBalance = account.getBalance() - amount;
        account.setBalance(totalBalance);
        Account savedAccount = accountRepository.save(account);
        log.info("Account balance withdraw successfully for {}. Current balance {}", savedAccount.getAccountHolderName(),
                savedAccount.getBalance());
        return savedAccount;
    }

    @Override
    public void deleteAccountById(long accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (!accountOptional.isPresent()) {
            throw new ResourceNotFoundException("Account not found with ID: " + accountId);
        }
        accountRepository.deleteById(accountId);
    }

    @Override
    public void deleteAllAccount() {
        List<Account> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException("No accounts found to delete");
        }
        accountRepository.deleteAll();
    }
}
