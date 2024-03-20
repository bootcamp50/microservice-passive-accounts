package com.nttdata.microservice.bankpassiveaccounts.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.microservice.bankpassiveaccounts.collections.DebitCardCollection;
import com.nttdata.microservice.bankpassiveaccounts.repository.IDebitCardRepository;
import com.nttdata.microservice.bankpassiveaccounts.services.IDebitCardService;

import reactor.core.publisher.Mono;

@Service
public class DebitCardServiceImpl implements  IDebitCardService{
	
	@Autowired
	private IDebitCardRepository repository;
	
	@Override
	public Mono<DebitCardCollection> save(DebitCardCollection collection) {
		collection.setDebitCardNumber(UUID.randomUUID().toString());
		return repository.save(collection);
	}

	@Override
	public Mono<DebitCardCollection> updateMainAccountNumber(String debitCardNumber, String accountNumber) {
		return repository.findByDebitCardNumber(debitCardNumber).next().flatMap(collection -> {
			collection.setMainAccountNumber(accountNumber);
			return repository.save(collection);
		});
	}

	@Override
	public Mono<String> getMainAccountNumber(String debitCardNumber) {
		return repository.findByDebitCardNumber(debitCardNumber).next().flatMap(collection -> {
			return Mono.just(collection.getMainAccountNumber());
		});
	}

	

}
