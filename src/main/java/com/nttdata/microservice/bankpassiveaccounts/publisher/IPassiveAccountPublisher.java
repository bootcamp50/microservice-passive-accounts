package com.nttdata.microservice.bankpassiveaccounts.publisher;

import reactor.core.publisher.Mono;

public interface IPassiveAccountPublisher {

	public Mono<Void> updateReport();
}
