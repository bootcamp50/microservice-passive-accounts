package com.nttdata.microservice.bankpassiveaccounts.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nttdata.microservice.bankpassiveaccounts.collections.DebitCardCollection;
import com.nttdata.microservice.bankpassiveaccounts.repository.IDebitCardRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class DebitCardServiceImplTest {
	
	@InjectMocks
	DebitCardServiceImpl service;
	
	@Mock
	IDebitCardRepository repository;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	@DisplayName("Test save a debit card")
	void save() {
		
		DebitCardCollection debitCard = new DebitCardCollection();
		debitCard.setDebitCardNumber("123456");
		debitCard.setPersonCode("123456");
		
		when(repository.save(debitCard)).thenReturn(Mono.just(debitCard));
		
		Mono<DebitCardCollection> expected = service.save(debitCard);
		assertNotNull(expected);
	}

	@Test
	@DisplayName("Test update main account of an debit card")
	void updateMainAccountNumber() {
		
		String debitCardNumber = "12346";
		String accountNumber = "123789";
		String personCode = "12356";
		
		DebitCardCollection debitCard = new DebitCardCollection();
		debitCard.setDebitCardNumber(debitCardNumber);
		debitCard.setPersonCode(personCode);
		
		when(repository.findByDebitCardNumber(debitCardNumber)).thenReturn(Flux.just(debitCard));
		when(repository.save(debitCard)).thenReturn(Mono.just(debitCard));
		
		Mono<DebitCardCollection> expected = service.updateMainAccountNumber(debitCardNumber, accountNumber);
		assertNotNull(expected);
	}

	@Test
	@DisplayName("Test get main account number of an debit card")
	void getMainAccountNumber() {
		
		String debitCardNumber = "12346";
		String mainAccountNumber = "12389";
		String personCode = "12356";
		
		DebitCardCollection debitCard = new DebitCardCollection();
		debitCard.setDebitCardNumber(debitCardNumber);
		debitCard.setPersonCode(personCode);
		debitCard.setMainAccountNumber(mainAccountNumber);
		
		when(repository.findByDebitCardNumber(debitCardNumber)).thenReturn(Flux.just(debitCard));
		Mono<String> expected = service.getMainAccountNumber(debitCardNumber);
		assertNotNull(expected);
		assertEquals("12389", expected.block());
	}

}
