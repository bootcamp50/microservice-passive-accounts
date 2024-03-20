package com.nttdata.microservice.bankpassiveaccounts.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.microservice.bankpassiveaccounts.collections.PassiveAccountCollection;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface IPassiveAccountRepository extends ReactiveCrudRepository<PassiveAccountCollection, ObjectId> {
	
	public Flux<PassiveAccountCollection> findByAccountNumber(String accountNumber);
	public Flux<PassiveAccountCollection> findByPersonCode(String personCode);
	public Mono<Long> countByPersonCode(String personCode);
}
