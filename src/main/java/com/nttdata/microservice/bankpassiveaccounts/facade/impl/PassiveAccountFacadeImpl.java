package com.nttdata.microservice.bankpassiveaccounts.facade.impl;

import org.springframework.stereotype.Service;

import com.nttdata.microservice.bankpassiveaccounts.facade.IPassiveAccountFacade;

import reactor.core.publisher.Mono;


@Service
public class PassiveAccountFacadeImpl implements  IPassiveAccountFacade{

	@Override
	public Mono<Boolean> checkIfHaveDebt(String personCode) {
		// TODO Auto-generated method stub
		return Mono.just(false);
	}

	@Override
	public Mono<Boolean> checkIfCreditCard(String personCode) {
		// TODO Auto-generated method stub
		return Mono.just(true);
	}

}
