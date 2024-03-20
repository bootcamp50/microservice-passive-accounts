package com.nttdata.microservice.bankpassiveaccounts.services.impl;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nttdata.microservice.bankpassiveaccounts.collections.PassiveAccountCollection;
import com.nttdata.microservice.bankpassiveaccounts.collections.enums.PassiveAccountTypeEnum;
import com.nttdata.microservice.bankpassiveaccounts.facade.IPassiveAccountFacade;
import com.nttdata.microservice.bankpassiveaccounts.repository.IPassiveAccountRepository;
import com.nttdata.microservice.bankpassiveaccounts.services.IMovementService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class PassiveAccountServiceImplTest {
	
	@InjectMocks
	private PassiveAccountServiceImpl service;
	
	@Mock
	IPassiveAccountRepository repository;
	
	@Mock
	IPassiveAccountFacade facade;
	
	@Mock
	IMovementService movementService;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}
	
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
		
		when(repository.countByPersonCode("123456")).thenReturn(Mono.just(0L));
		when(facade.checkIfHaveDebt("123456")).thenReturn(Mono.just(false));
		when(repository.save(account)).thenReturn(accountMono);
		
		Mono<PassiveAccountCollection> expected = service.saveCurrentPersonalAccount(account);
		assertNotNull(expected);
		assertEquals("123456", expected.block().getPersonCode());
		
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
		
		when(repository.countByPersonCode("123456")).thenReturn(Mono.just(0L));
		when(facade.checkIfHaveDebt("123456")).thenReturn(Mono.just(false));
		when(repository.save(account)).thenReturn(accountMono);
		
		Mono<PassiveAccountCollection> expected = service.saveSavingPersonalAccount(account);
		assertNotNull(expected);
		assertEquals("123456", expected.block().getPersonCode());
		
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
		
		when(repository.countByPersonCode("123456")).thenReturn(Mono.just(0L));
		when(facade.checkIfHaveDebt("123456")).thenReturn(Mono.just(false));
		when(repository.save(account)).thenReturn(accountMono);
		
		Mono<PassiveAccountCollection> expected = service.saveFixTermPersonalAccount(account);
		assertNotNull(expected);
		assertEquals("123456", expected.block().getPersonCode());
		
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
		
		when(facade.checkIfHaveDebt("123456")).thenReturn(Mono.just(false));
		when(repository.save(account)).thenReturn(accountMono);
		
		Mono<PassiveAccountCollection> expected = service.saveCurrentEnterpriseAccount(account);
		assertNotNull(expected);
		assertEquals("123456", expected.block().getPersonCode());
		
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
		
		when(repository.findByAccountNumber("123456")).thenReturn(Flux.just(account));
		when(movementService.checkIfHaveAverageAmount("123456",0.0)).thenReturn(Mono.just(true));
		when(facade.checkIfHaveDebt("123456")).thenReturn(Mono.just(false));
		when(facade.checkIfCreditCard("123456")).thenReturn(Mono.just(true));
		when(repository.save(account)).thenReturn(accountMono);
		
		Mono<PassiveAccountCollection> expected = service.saveVipPersonalAccount("123456",0.0);
		assertNotNull(expected);
		assertEquals("123456", expected.block().getPersonCode());
		
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
		
		when(repository.findByAccountNumber("123456")).thenReturn(Flux.just(account));
		when(facade.checkIfHaveDebt("123456")).thenReturn(Mono.just(false));
		when(facade.checkIfCreditCard("123456")).thenReturn(Mono.just(true));
		when(repository.save(account)).thenReturn(accountMono);
		
		Mono<PassiveAccountCollection> expected = service.savePymeEnterpriseAccount("123456");
		assertNotNull(expected);
		assertEquals("123456", expected.block().getPersonCode());
		
	}
	
	
	
	
	
	
	@Test
	@DisplayName("Test If have more than one account")
	void checkIfHaveMoreThanOneAccountTest() {
		
		Mono<Long> accounts = Mono.just(1L); 
		
		when(repository.countByPersonCode("123456")).thenReturn(accounts);
		
		Mono<Boolean> expected = service.checkIfHaveMoreThanOneAccount("123456");
		
		assertNotNull(expected);
		assertEquals(true, expected.block());
		
	}
	
	@Test
	@DisplayName("Test If exist one account")
	void checkIfExistTest() {
		
		PassiveAccountCollection account = new PassiveAccountCollection();
		account.setAccountNumber("123456");
		account.setAccountBalance(1000.0);
		PassiveAccountCollection[] lista = {account};
		Flux<PassiveAccountCollection> flux = Flux.fromArray(lista);
		
		when(repository.findByAccountNumber("123456")).thenReturn(flux);
		
		Mono<Boolean> expected = service.checkIfExist("123456");
		
		assertNotNull(expected);
		assertEquals(true, expected.block());
		
	}
	
	@Test
	@DisplayName("Test update account balance of one account")
	void updateAccountBalanceTest() {
		
		PassiveAccountCollection account = new PassiveAccountCollection();
		account.setAccountNumber("123456");
		account.setAccountBalance(1000.0);
		
		when(repository.findByAccountNumber("123456")).thenReturn(Flux.just(account));
		when(repository.save(account)).thenReturn(Mono.just(account));
		
		Mono<PassiveAccountCollection> expected = service.updateAccountBalance("123456",1500.0);
		
		assertNotNull(expected);
		assertEquals("123456", expected.block().getAccountNumber());
	}
	
	@Test
	@DisplayName("Test update debit card number of one account")
	void updateDebitCardNumberTest() {
		
		PassiveAccountCollection account = new PassiveAccountCollection();
		account.setAccountNumber("123456");
		account.setAccountBalance(1000.0);
		
		when(repository.findByAccountNumber("123456")).thenReturn(Flux.just(account));
		when(repository.save(account)).thenReturn(Mono.just(account));
		
		Mono<PassiveAccountCollection> expected = service.updateDebitCardNumber("123456","123789");
		
		assertNotNull(expected);
		assertEquals("123456", expected.block().getAccountNumber());
	}
	
	@Test
	@DisplayName("Test get account number available of one person")
	void getAccountNumberAvailableTest() {
		
		PassiveAccountCollection account = new PassiveAccountCollection();
		account.setPersonCode("123789");
		account.setAccountNumber("123456");
		account.setAccountBalance(1000.0);
		
		when(repository.findByPersonCode("123789")).thenReturn(Flux.just(account));
		
		Mono<String> expected = service.getAccountNumberAvailable("123789",10.0);
		
		assertNotNull(expected);
		assertEquals("123456", expected.block());
	}
	
	
	@Test
	@DisplayName("Test get minimum openning amount by product")
	void getMinimumOpeningAmountTest() {
		
		String passiveAccountType = PassiveAccountTypeEnum.CURRENT_ACCOUNT.toString(); 
		
		Mono<Double> expected = service.getMinimumOpeningAmount(passiveAccountType);
		
		assertNotNull(expected);
		assertEquals(1.0, expected.block());
		
	}
	

	@Test
	@DisplayName("Test get account balance by accountNumber")
	void getAccountBalanceTest() {

		PassiveAccountCollection account = new PassiveAccountCollection();
		account.setAccountNumber("123456");
		account.setAccountBalance(1000.0);
		PassiveAccountCollection[] lista = {account};
		Flux<PassiveAccountCollection> flux = Flux.fromArray(lista);
		
		when(repository.findByAccountNumber("123456")).thenReturn(flux);
		
		Mono<Double> expected = service.getAccountBalance("123456");
		
		assertNotNull(expected);
		assertEquals(1000.0, expected.block());
		
	}
	
	@Test
	@DisplayName("Test get day movement available by accountNumber")
	void getDayMovementAvailableTest() {

		PassiveAccountCollection account = new PassiveAccountCollection();
		account.setAccountNumber("123456");
		account.setDayMovementAvailable(2);
		PassiveAccountCollection[] lista = {account};
		Flux<PassiveAccountCollection> flux = Flux.fromArray(lista);
		
		when(repository.findByAccountNumber("123456")).thenReturn(flux);
		
		Mono<Integer> expected = service.getDayMovementAvailable("123456");
		
		assertNotNull(expected);
		assertEquals(2, expected.block());
		
	}
	
	@Test
	@DisplayName("Test get maximum transaction by accountNumber")
	void getMaximumTransactionsTest() {

		PassiveAccountCollection account = new PassiveAccountCollection();
		account.setAccountNumber("123456");
		account.setMaximumTransactions(20);
		PassiveAccountCollection[] lista = {account};
		Flux<PassiveAccountCollection> flux = Flux.fromArray(lista);
		
		when(repository.findByAccountNumber("123456")).thenReturn(flux);
		
		Mono<Integer> expected = service.getMaximumTransactions("123456");
		
		assertNotNull(expected);
		assertEquals(20, expected.block());
		
	}
	
	@Test
	@DisplayName("Test get maximum transaction without commission by accountNumber")
	void getMaximumTransactionsWithoutCommissionTest() {

		PassiveAccountCollection account = new PassiveAccountCollection();
		account.setAccountNumber("123456");
		account.setMaximumTransactionsWithoutCommission(10);
		PassiveAccountCollection[] lista = {account};
		Flux<PassiveAccountCollection> flux = Flux.fromArray(lista);
		
		when(repository.findByAccountNumber("123456")).thenReturn(flux);
		
		Mono<Integer> expected = service.getMaximumTransactionsWithoutCommission("123456");
		
		assertNotNull(expected);
		assertEquals(10, expected.block());
		
	}
	
	@Test
	@DisplayName("Test get maintenance commission by accountNumber")
	void getMaintenanceCommissionTest() {

		PassiveAccountCollection account = new PassiveAccountCollection();
		account.setAccountNumber("123456");
		account.setMaintenanceCommission(10.0);
		PassiveAccountCollection[] lista = {account};
		Flux<PassiveAccountCollection> flux = Flux.fromArray(lista);
		
		when(repository.findByAccountNumber("123456")).thenReturn(flux);
		
		Mono<Double> expected = service.getMaintenanceCommission("123456");
		
		assertNotNull(expected);
		assertEquals(10.0, expected.block());
		
	}
	
	

}
