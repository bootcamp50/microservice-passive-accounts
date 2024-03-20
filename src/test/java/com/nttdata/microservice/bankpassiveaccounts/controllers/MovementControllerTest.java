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

import com.nttdata.microservice.bankpassiveaccounts.collections.MovementsCollection;
import com.nttdata.microservice.bankpassiveaccounts.collections.PassiveAccountCollection;
import com.nttdata.microservice.bankpassiveaccounts.services.IMovementService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@WebFluxTest(MovementController.class)
class MovementControllerTest {
	
	@Autowired
	private WebTestClient webTestClient;
	
	@MockBean
	private IMovementService service;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	@DisplayName("Test get movements of an account")
	void getByAccountNumber(){
		
		MovementsCollection movement = new MovementsCollection();
		movement.setAccountNumberSource("123456");
		movement.setAccountNumberDestination("123789");
		movement.setAmount(100.0);
		
		MovementsCollection movement2 = new MovementsCollection();
		movement2.setAccountNumberSource("123456");
		movement2.setAccountNumberDestination("123789");
		movement2.setAmount(100.0);
		
		Flux<MovementsCollection> movements = Flux.just(movement, movement2);
		
		when(service.getByAccountNumber("123456")).thenReturn(movements);
		
		Flux<MovementsCollection> response = webTestClient.get().uri("/movement/getByAccountNumber/123456")
				.exchange()
				.expectStatus().isOk()
				.returnResult(MovementsCollection.class)
				.getResponseBody();
				
				StepVerifier.create(response)
				.expectSubscription()
				.expectNext(movement)
				.expectNext(movement2)
				.verifyComplete();
		
	}
	
	@Test
	@DisplayName("Test save deposit")
	void saveDeposit() {
		
		String accountNumber = "123456";
		MovementsCollection movement = new MovementsCollection();
		movement.setAccountNumberDestination(accountNumber);
		movement.setAmount(100.0);
		Mono<MovementsCollection> movementMono = Mono.just(movement);
		
		when(service.saveDeposit(movement)).thenReturn(Mono.empty());
		
		webTestClient.post().uri("/movement/saveDeposit")
		.body(Mono.just(movementMono),PassiveAccountCollection.class)
		.exchange()
		.expectStatus().isOk();
		
		
		
	}
	
	@Test
	@DisplayName("Test save withdrawal")
	void saveWithdrawal() {
		
		String accountNumber = "123456";
		MovementsCollection movement = new MovementsCollection();
		movement.setAccountNumberDestination(accountNumber);
		movement.setAmount(100.0);
		Mono<MovementsCollection> movementMono = Mono.just(movement);
		
		when(service.saveDeposit(movement)).thenReturn(Mono.empty());
		
		webTestClient.post().uri("/movement/saveWithdrawal")
		.body(Mono.just(movementMono),PassiveAccountCollection.class)
		.exchange()
		.expectStatus().isOk();
		
	}
	
	@Test
	@DisplayName("Test transfer with same account")
	void saveTransferWithSameAccount() {
		String sourceAccountNumber = "123456";
		String destinationAccountNumber = "123789";
		MovementsCollection movement = new MovementsCollection();
		movement.setAccountNumberSource(sourceAccountNumber);
		movement.setAccountNumberDestination(destinationAccountNumber);
		movement.setAmount(100.0);
		Mono<MovementsCollection> movementMono = Mono.just(movement);
		
		when(service.saveDeposit(movement)).thenReturn(Mono.empty());
		
		webTestClient.post().uri("/movement/saveTransferWithSameAccount")
		.body(Mono.just(movementMono),PassiveAccountCollection.class)
		.exchange()
		.expectStatus().isOk();
	}
	
	@Test
	@DisplayName("Test transfer with third account")
	void saveTransferThirdAccount() {
		String sourceAccountNumber = "123456";
		String destinationAccountNumber = "123789";
		MovementsCollection movement = new MovementsCollection();
		movement.setAccountNumberSource(sourceAccountNumber);
		movement.setAccountNumberDestination(destinationAccountNumber);
		movement.setAmount(100.0);
		Mono<MovementsCollection> movementMono = Mono.just(movement);
		
		when(service.saveDeposit(movement)).thenReturn(Mono.empty());
		
		webTestClient.post().uri("/movement/saveTransferThirdAccount")
		.body(Mono.just(movementMono),PassiveAccountCollection.class)
		.exchange()
		.expectStatus().isOk();
	}
	
	@Test
	@DisplayName("Test transfer with debit card")
	void saveWithdrawalWithDebitCard() {
		
		String debitCardNumber = "123456";
		String destinationAccountNumber = "123789";
		String personCode = "123456";
		
		MovementsCollection movement = new MovementsCollection();
		movement.setDebitCardNumber(debitCardNumber);
		movement.setPersonCode(personCode);
		movement.setAccountNumberDestination(destinationAccountNumber);
		movement.setAmount(100.0);
		Mono<MovementsCollection> movementMono = Mono.just(movement);
		
		when(service.saveDeposit(movement)).thenReturn(Mono.empty());
		
		webTestClient.post().uri("/movement/saveWithdrawalWithDebitCard")
		.body(Mono.just(movementMono),PassiveAccountCollection.class)
		.exchange()
		.expectStatus().isOk();
	}
	

}
