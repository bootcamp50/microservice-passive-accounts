package com.nttdata.microservice.bankpassiveaccounts.services;

import com.nttdata.microservice.bankpassiveaccounts.collections.DebitCardCollection;

import reactor.core.publisher.Mono;

public interface IDebitCardService {
	
	public Mono<DebitCardCollection> save(DebitCardCollection collection);
	public Mono<DebitCardCollection> updateMainAccountNumber(String debitCardNumber, String accountNumber);
	public Mono<String> getMainAccountNumber(String debitCardNumber);
	
	}
