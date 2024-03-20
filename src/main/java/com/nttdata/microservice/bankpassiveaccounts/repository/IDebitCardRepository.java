package com.nttdata.microservice.bankpassiveaccounts.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.microservice.bankpassiveaccounts.collections.DebitCardCollection;

import reactor.core.publisher.Flux;

@Repository
public interface IDebitCardRepository extends ReactiveCrudRepository<DebitCardCollection, ObjectId>{

	public Flux<DebitCardCollection> findByDebitCardNumber(String debitCardNumber);
}
