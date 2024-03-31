package com.workshop.banking.mapper;

import com.workshop.banking.dto.AccountDto;
import com.workshop.banking.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public abstract class AccountMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "accountHolderName", source = "accountHolderName")
    @Mapping(target = "balance", source = "balance")
    public abstract Account mapToEntity(AccountDto accountDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "accountHolderName", source = "accountHolderName")
    @Mapping(target = "balance", source = "balance")
    public abstract AccountDto mapToDto(Account account);
}
