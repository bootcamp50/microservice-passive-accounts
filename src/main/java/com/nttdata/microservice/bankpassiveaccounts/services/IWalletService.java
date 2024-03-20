package com.nttdata.microservice.bankpassiveaccounts.services;

import com.nttdata.microservice.bankpassiveaccounts.collections.WalletCollection;

import reactor.core.publisher.Mono;

public interface IWalletService {
	
	public Mono<WalletCollection> save(WalletCollection collection);
	public Mono<WalletCollection> updateDebitCardNumber(String walletNumber, String debitCardNumber);
	public Mono<String> getDebitCardNumber(String walletNumber);

}
