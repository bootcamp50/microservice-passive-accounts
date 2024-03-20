package com.nttdata.microservice.bankpassiveaccounts.services;

import java.util.Date;

import com.nttdata.microservice.bankpassiveaccounts.collections.PassiveAccountCollection;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPassiveAccountService {
	
	
	//SAVE ACCOUNTS
	public Mono<PassiveAccountCollection> saveCurrentPersonalAccount(PassiveAccountCollection collection);
	public Mono<PassiveAccountCollection> saveSavingPersonalAccount(PassiveAccountCollection collection);
	public Mono<PassiveAccountCollection> saveFixTermPersonalAccount(PassiveAccountCollection collection);
	public Mono<PassiveAccountCollection> saveCurrentEnterpriseAccount(PassiveAccountCollection collection);
	
	public Mono<PassiveAccountCollection> saveVipPersonalAccount(String accountNumber, Double minimumAverageAmount);
	public Mono<PassiveAccountCollection> savePymeEnterpriseAccount(String accountNumber);
	
	//VALIDATORS
	public Mono<Boolean> checkIfHaveMoreThanOneAccount(String personCode);
	
	
	//EXTERNAL VALIDATORS 
	public Mono<Boolean> checkIfExist(String accountNumber);
	public Mono<Double> getAccountBalance(String accountNumber);
	
	//UPDATE
	public Mono<PassiveAccountCollection> updateAccountBalance(String accountNumber, Double newAccountBalance);
	
	//USING IN ACCOUNT
	public Mono<Double> getMinimumOpeningAmount(String accountType);
	
	//USING IN MOVEMENT
	public Mono<Integer> getMaximumTransactions(String accountNumber);
	public Mono<Integer> getMaximumTransactionsWithoutCommission(String accountNumber);
	public Mono<Integer> getDayMovementAvailable(String accountNumber);
	public Mono<Double> getTransactionCommission(String accountNumber);
	
	public Mono<String> getAccountNumberAvailable(String personCode, Double movementAmount);
	
	///
	
	public Mono<Double> getMaintenanceCommission(String accountNumber);
	public Flux<PassiveAccountCollection> getPassiveAccountsWithChargeCommissionPending(Date chargeCommissionDate );
	public Mono<PassiveAccountCollection> updateChargeCommissionDate(String accountNumber, Date chargeCommissionDate);
	
	
	
	public Mono<PassiveAccountCollection> updateDebitCardNumber(String accountNumber, String debitCardNumber); 
	
}
