package com.nttdata.microservice.bankpassiveaccounts.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.microservice.bankpassiveaccounts.collections.WalletCollection;
import com.nttdata.microservice.bankpassiveaccounts.repository.IWalletRepository;
import com.nttdata.microservice.bankpassiveaccounts.services.IWalletService;

import reactor.core.publisher.Mono;

@Service
public class WalletServiceImpl implements  IWalletService{
	
	@Autowired
	private IWalletRepository repository;

	@Override
	public Mono<WalletCollection> save(WalletCollection collection) {
		collection.setWalletNumber(UUID.randomUUID().toString());
		return repository.save(collection);
	}

	@Override
	public Mono<WalletCollection> updateDebitCardNumber(String walletNumber, String debitCardNumber) {
		return repository.findByWalletNumber(walletNumber).next().flatMap(collection -> {
			collection.setDebitCardNumber(debitCardNumber);
			return repository.save(collection);
		});
	}

	@Override
	public Mono<String> getDebitCardNumber(String walletNumber) {
		return repository.findByWalletNumber(walletNumber).next().flatMap(collection -> {
			return Mono.just(collection.getDebitCardNumber());
		});
	}

}
