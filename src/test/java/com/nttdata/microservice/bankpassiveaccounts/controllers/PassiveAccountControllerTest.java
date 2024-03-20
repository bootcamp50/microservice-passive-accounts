package com.nttdata.microservice.bankpassiveaccounts.controllers;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.nttdata.microservice.bankpassiveaccounts.collections.PassiveAccountCollection;
import com.nttdata.microservice.bankpassiveaccounts.services.IPassiveAccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@WebFluxTest(PassiveAccountController.class)
class PassiveAccountControllerTest {

	@Autowired
	private WebTestClient webTestClient;
	
	@MockBean
	private IPassiveAccountService service;
	
	@Test
	@DisplayName("Test save current personal account")
	void saveCurrentPersonalAccountTest() {
		
		PassiveAccountCollection account = new PassiveAccountCollection();
		account.setPersonCode("123456");
		account.setAccountAmount(1000.0);
		account.setAccountBalance(1000.0);
		account.setMaintenanceCommission(10.0);
		account.setTransactionCommission(10.0);
		account.setMaximumTransactionsWithoutCommission(10);
		Mono<PassiveAccountCollection> accountMono = Mono.just(account);
		
		when(service.saveCurrentPersonalAccount(account)).thenReturn(accountMono);
		
		webTestClient.post().uri("/passive-accounts/saveCurrentPersonalAccount")
		.body(Mono.just(accountMono),PassiveAccountCollection.class)
		.exchange()
		.expectStatus().isOk();
		
	}
	
	@Test
	@DisplayName("Test save saving personal account")
	void saveSavingPersonalAccountTest() {

		PassiveAccountCollection account = new PassiveAccountCollection();
		account.setPersonCode("123456");
		account.setAccountAmount(1000.0);
		account.setAccountBalance(1000.0);
		account.setMaximumTransactions(200);
		account.setTransactionCommission(10.0);
		account.setMaximumTransactionsWithoutCommission(10);
		Mono<PassiveAccountCollection> accountMono = Mono.just(account);
		
		when(service.saveSavingPersonalAccount(account)).thenReturn(accountMono);
		
		webTestClient.post().uri("/passive-accounts/saveSavingPersonalAccount")
		.body(Mono.just(accountMono),PassiveAccountCollection.class)
		.exchange()
		.expectStatus().isOk();
		
	}
	
	
	@Test
	@DisplayName("Test save fix term personal account")
	void saveFixTermPersonalAccountTest() {

		PassiveAccountCollection account = new PassiveAccountCollection();
		account.setPersonCode("123456");
		account.setAccountAmount(1000.0);
		account.setAccountBalance(1000.0);
		
		account.setMaximumTransactions(1);
		account.setDayMovementAvailable(2);
		
		account.setTransactionCommission(10.0);
		account.setMaximumTransactionsWithoutCommission(10);
		
		Mono<PassiveAccountCollection> accountMono = Mono.just(account);
		
		when(service.saveFixTermPersonalAccount(account)).thenReturn(accountMono);
		
		webTestClient.post().uri("/passive-accounts/saveFixTermPersonalAccount")
		.body(Mono.just(accountMono),PassiveAccountCollection.class)
		.exchange()
		.expectStatus().isOk();
		
	}
	
	@Test
	@DisplayName("Test save current enterprise account")
	void saveCurrentEnterpriseAccountTest() {

		PassiveAccountCollection account = new PassiveAccountCollection();
		account.setPersonCode("123456");
		account.setAccountAmount(1000.0);
		account.setAccountBalance(1000.0);
				
		account.setTransactionCommission(10.0);
		account.setMaximumTransactionsWithoutCommission(10);
		
		Mono<PassiveAccountCollection> accountMono = Mono.just(account);
		
		when(service.saveCurrentEnterpriseAccount(account)).thenReturn(accountMono);
		
		webTestClient.post().uri("/passive-accounts/saveCurrentEnterpriseAccount")
		.body(Mono.just(accountMono),PassiveAccountCollection.class)
		.exchange()
		.expectStatus().isOk();
		
	}
	
	@Test
	@DisplayName("Test save vip personal account")
	void saveVipPersonalAccountTest() {

		PassiveAccountCollection account = new PassiveAccountCollection();
		account.setAccountNumber("123789");
		account.setPersonCode("123456");
		account.setAccountAmount(1000.0);
		account.setAccountBalance(1000.0);
				
		account.setTransactionCommission(10.0);
		account.setMaximumTransactionsWithoutCommission(10);
		
		Mono<PassiveAccountCollection> accountMono = Mono.just(account);
		
		when(service.saveVipPersonalAccount("123789",0.0)).thenReturn(accountMono);
		
		webTestClient.post().uri("/passive-accounts/saveVipPersonalAccount/123789/0.0")
		.body(Mono.just(accountMono),PassiveAccountCollection.class)
		.exchange()
		.expectStatus().isOk();
		
		
	}
	
	@Test
	@DisplayName("Test save pyme enterprise account")
	void savePymeEnterpriseAccountTest() {

		PassiveAccountCollection account = new PassiveAccountCollection();
		account.setAccountNumber("123789");
		account.setPersonCode("123456");
		account.setAccountAmount(1000.0);
		account.setAccountBalance(1000.0);
				
		account.setTransactionCommission(10.0);
		account.setMaximumTransactionsWithoutCommission(10);
		
		Mono<PassiveAccountCollection> accountMono = Mono.just(account);
		
		when(service.savePymeEnterpriseAccount("123789")).thenReturn(accountMono);

		webTestClient.post().uri("/passive-accounts/savePymeEnterpriseAccount/123789")
		.body(Mono.just(accountMono),PassiveAccountCollection.class)
		.exchange()
		.expectStatus().isOk();
		
	}
	
	
	@Test
	@DisplayName("Test get account balance")
	void getAccountBalanceTest() {

		
		when(service.getAccountBalance("123789")).thenReturn(Mono.just(11.0));

		Flux<Double> response = webTestClient.get().uri("/passive-accounts/getAccountBalance/123789")
		.exchange()
		.expectStatus().isOk()
		.returnResult(Double.class)
		.getResponseBody();
		
		StepVerifier.create(response)
		.expectSubscription()
		.expectNext(11.0)
		.verifyComplete();
		
	}
	
	@Test
	@DisplayName("Test check if account exist")
	void checkIfExistTest() {

		
		when(service.checkIfExist("123789")).thenReturn(Mono.just(true));

		Flux<Boolean> response = webTestClient.get().uri("/passive-accounts/checkIfExist/123789")
		.exchange()
		.expectStatus().isOk()
		.returnResult(Boolean.class)
		.getResponseBody();
		
		StepVerifier.create(response)
		.expectSubscription()
		.expectNext(true)
		.verifyComplete();
		
	}
	
	@Test
	@DisplayName("Test associate account to debit card")
	void updateCreditDebitNumberTest() {

		PassiveAccountCollection account = new PassiveAccountCollection();
		account.setAccountNumber("123789");
		account.setPersonCode("123456");
		account.setAccountAmount(1000.0);
		account.setAccountBalance(1000.0);
				
		account.setTransactionCommission(10.0);
		account.setMaximumTransactionsWithoutCommission(10);
		
		Mono<PassiveAccountCollection> accountMono = Mono.just(account);
		
		when(service.updateDebitCardNumber("123789","123456")).thenReturn(accountMono);

		webTestClient.post().uri("/passive-accounts/updateCreditDebitNumber/123789/123456")
		.exchange()
		.expectStatus().isOk();
		
	}
	
	
	

}
