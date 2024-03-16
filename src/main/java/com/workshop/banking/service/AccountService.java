package com.workshop.banking.service;

import com.workshop.banking.dto.AccountDto;
import com.workshop.banking.model.BankingResponse;
import org.springframework.http.ResponseEntity;

public interface AccountService {

    ResponseEntity<BankingResponse> createAccount(AccountDto accountDto);

    ResponseEntity<BankingResponse> getAccountById(long accountId);

    ResponseEntity<BankingResponse> getAccounts(int pageNumber, int pageSize);
}
