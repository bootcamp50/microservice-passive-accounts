package com.nttdata.microservice.bankpassiveaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentAccountPersonalDto {

	private String accountNumber;
	private String personCode;
	private Double accountAmount;
	private Double accountBalance;
	private Double maintenanceCommission;
	private Double transactionCommission;
	private Integer maximumTransactionsWithoutCommission;
	
}
