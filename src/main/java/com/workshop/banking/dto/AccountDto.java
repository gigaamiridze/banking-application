package com.workshop.banking.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    @Positive
    private Long id;

    @NotNull
    @NotEmpty
    private String accountHolderName;

    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "1000000.0")
    private double balance;
}
