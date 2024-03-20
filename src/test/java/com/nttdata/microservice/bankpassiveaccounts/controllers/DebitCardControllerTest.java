package com.nttdata.microservice.bankpassiveaccounts.controllers;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.nttdata.microservice.bankpassiveaccounts.collections.DebitCardCollection;
import com.nttdata.microservice.bankpassiveaccounts.services.IDebitCardService;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@WebFluxTest(DebitCardController.class)
class DebitCardControllerTest {

	@Autowired
	private WebTestClient webTestClient;
	
	@MockBean
	private IDebitCardService service;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	@DisplayName("Test save debit cards")
	void save() {
		
		DebitCardCollection debitCard = new DebitCardCollection();
		debitCard.setDebitCardNumber("123456");
		debitCard.setPersonCode("123456");
		Mono<DebitCardCollection> debitCardMono = Mono.just(debitCard) ;
		
		when(service.save(debitCard)).thenReturn(Mono.just(debitCard));
		
		webTestClient.post().uri("/debitcard/save")
		.body(Mono.just(debitCardMono),DebitCardCollection.class)
		.exchange()
		.expectStatus().isOk();
	}
	
	@Test
	@DisplayName("Test update main account of an credit card")
	void updateMainAccountNumber() {
	
		DebitCardCollection debitCard = new DebitCardCollection();
		debitCard.setDebitCardNumber("123456");
		debitCard.setPersonCode("123789");
		Mono<DebitCardCollection> debitCardMono = Mono.just(debitCard) ;
		
		when(service.save(debitCard)).thenReturn(debitCardMono);
		
		webTestClient.post().uri("/debitcard/updateMainAccountNumber/123456/123123")
		.exchange()
		.expectStatus().isOk();
		
		
	}

}
