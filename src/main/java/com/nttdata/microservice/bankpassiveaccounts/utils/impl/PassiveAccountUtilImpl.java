package com.nttdata.microservice.bankpassiveaccounts.utils.impl;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.nttdata.microservice.bankpassiveaccounts.collections.Customer;
import com.nttdata.microservice.bankpassiveaccounts.collections.PassiveAccountCollection;
import com.nttdata.microservice.bankpassiveaccounts.dto.PassiveAccountBalanceDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.PassiveAccountCreateDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.PassiveAccountDto;
import com.nttdata.microservice.bankpassiveaccounts.utils.PassiveAccountUtil;

@Component
public class PassiveAccountUtilImpl implements PassiveAccountUtil {

	@Override
	public PassiveAccountCollection toPassiveAccountCollection(PassiveAccountCreateDto accountDto) {
		return PassiveAccountCollection.builder()
                .customer(Customer.builder().id(accountDto.getCustomerId()).build())
                .accountType(accountDto.getAccountType())
                .accountInfo(accountDto.getAccountInfo())
                .accountNumber(UUID.randomUUID().toString())
                .issueDate(accountDto.getIssueDate())
                .dueDate(accountDto.getDueDate())
                .holders(accountDto.getHolders())
                .signers(accountDto.getSigners())
                .build();
	}

	@Override
	public PassiveAccountDto toPassiveAccountDto(PassiveAccountCollection account) {
		return PassiveAccountDto.builder()
				.id(String.valueOf(account.getId()))
		        .customerId(account.getCustomer().getId())
		        .accountType(account.getAccountType())
		        .accountInfo(account.getAccountInfo())
		        .accountNumber(account.getAccountNumber())
		        .issueDate(account.getIssueDate())
		        .state(account.getState())
		        .currentBalance(account.getCurrentBalance())
		        .doneOperationsInMonth(account.getDoneOperationsInMonth())
		        .movements(account.getMovements())
		        .dueDate(account.getDueDate())
		        .holders(account.getHolders())
		        .signers(account.getSigners())
		        .build();
	}
	
	@Override
    public PassiveAccountBalanceDto toPassiveAccountBalanceDto(PassiveAccountCollection account) {
        return PassiveAccountBalanceDto.builder()
                .id(String.valueOf(account.getId()))
                .currentBalance(account.getCurrentBalance())
                .accountType(account.getAccountType())
                .build();
    }

}
