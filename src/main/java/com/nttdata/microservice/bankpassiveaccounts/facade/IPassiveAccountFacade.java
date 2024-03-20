package com.nttdata.microservice.bankpassiveaccounts.facade;

import reactor.core.publisher.Mono;

public interface IPassiveAccountFacade {
	
	public Mono<Boolean> checkIfHaveDebt(String personCode);
	public Mono<Boolean> checkIfCreditCard(String personCode);

}
