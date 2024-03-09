package com.workshop.banking.mapper;

import com.workshop.banking.dto.AccountDto;
import com.workshop.banking.entity.Account;
import org.springframework.stereotype.Service;

@Service
public class AccountMapper {

    public static Account mapToEntity(AccountDto accountDto) {
        return Account.builder()
                .id(accountDto.getId())
                .accountHolderName(accountDto.getAccountHolderName())
                .balance(accountDto.getBalance())
                .build();
    }

    public static AccountDto mapToDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .accountHolderName(account.getAccountHolderName())
                .balance(account.getBalance())
                .build();
    }

}
