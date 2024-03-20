package com.nttdata.microservice.bankpassiveaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingAccountPersonalDto {

	private String accountNumber;
	private Double accountAmount;
	private Double accountBalance;
	private Double transactionCommission;
	private String personCode;
	private Integer maximumTransactions;
}
