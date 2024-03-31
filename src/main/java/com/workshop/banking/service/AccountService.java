package com.workshop.banking.service;

import com.workshop.banking.entity.Account;
import com.workshop.banking.model.PageableResponse;

public interface AccountService {

    Account createAccount(Account Account);

    Account getAccountById(long accountId);

    PageableResponse<Account> getAccounts(int pageNumber, int pageSize);

    Account depositBalance(long accountId, double amount);

    Account withdrawBalance(long accountId, double amount);

    void deleteAccountById(long accountId);

    void deleteAllAccount();
}
