package com.nttdata.microservice.bankpassiveaccounts.services;

import com.nttdata.microservice.bankpassiveaccounts.collections.MovementsCollection;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IMovementService {
	
	public Mono<Void> saveDeposit(MovementsCollection collection);
	public Mono<Void> saveWithdrawal(MovementsCollection collection);
	
	public Mono<MovementsCollection> saveTransferWithSameAccount(MovementsCollection collection);
	public Mono<MovementsCollection> saveTransferThirdAccount(MovementsCollection collection);
	
	public Mono<MovementsCollection> saveWithdrawalWithDebitCard(MovementsCollection collection);
	
	public Mono<MovementsCollection> saveWithdrawalWithWallet(MovementsCollection collection);
	
	public Flux<MovementsCollection> getByAccountNumber(String accountNumber);
	
	public Mono<Boolean> checkMaximumTransactions(String accountNumber);
	public Mono<Boolean> checkMaximumTransactionsWithoutCommission(String accountNumber);
	public Mono<Boolean> checkDayMovementAvailable(String accountNumber);
	
	public Mono<Boolean> checkIfHaveAverageAmount(String accountNumber, Double minimumAverageAmount);
	
	
	
}
