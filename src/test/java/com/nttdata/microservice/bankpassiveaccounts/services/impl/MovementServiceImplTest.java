package com.nttdata.microservice.bankpassiveaccounts.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nttdata.microservice.bankpassiveaccounts.collections.MovementsCollection;
import com.nttdata.microservice.bankpassiveaccounts.repository.IMovementRepository;
import com.nttdata.microservice.bankpassiveaccounts.services.IDebitCardService;
import com.nttdata.microservice.bankpassiveaccounts.services.IPassiveAccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


class MovementServiceImplTest {
	
	@InjectMocks
	MovementServiceImpl service;
	
	@Mock
	IMovementRepository repository;
	
	@Mock
	IDebitCardService debitCardService;
	
	@Mock
	IPassiveAccountService passiveAccountService;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}

		
	@Test
	@DisplayName("Test save a deposit")
	void saveDepositTest() {
		
		String accountNumber = "123456";
		Integer maximumTransactions = 10;
		Integer dayMovementAvailable = LocalDate.now().getDayOfMonth();
		Double amountBalance = 1000.0;
		Double transactionCommission = 10.0;
		Integer maximumTransactionsWithoutCommission = 10;
		
		MovementsCollection movement = new MovementsCollection();
		movement.setAccountNumberDestination(accountNumber);
		movement.setAmount(100.0);
		Flux<MovementsCollection> movements = Flux.just(movement);
		
		when(passiveAccountService.getMaximumTransactions(accountNumber)).thenReturn(Mono.just(maximumTransactions));
		when(repository.findAll()).thenReturn(movements);
		when(passiveAccountService.getDayMovementAvailable(accountNumber)).thenReturn(Mono.just(dayMovementAvailable));
		when(passiveAccountService.getAccountBalance(accountNumber)).thenReturn(Mono.just(amountBalance));
		when(passiveAccountService.getTransactionCommission(accountNumber)).thenReturn(Mono.just(transactionCommission));
		when(passiveAccountService.getMaximumTransactionsWithoutCommission(accountNumber)).thenReturn(Mono.just(maximumTransactionsWithoutCommission));
		when(repository.save(movement)).thenReturn(Mono.just(movement));
		when(passiveAccountService.updateAccountBalance(accountNumber,amountBalance)).thenReturn(Mono.empty());
		
		Mono<Void> expected = service.saveDeposit(movement);

		assertNotNull(expected);
		
	}
	
	@Test
	@DisplayName("Test save a withdrawal")
	void saveWithdrawalTest() {
		
		String accountNumber = "123456";
		Integer maximumTransactions = 10;
		Integer dayMovementAvailable = LocalDate.now().getDayOfMonth();
		Double amountBalance = 1000.0;
		Double transactionCommission = 10.0;
		Integer maximumTransactionsWithoutCommission = 10;
		
		MovementsCollection movement = new MovementsCollection();
		movement.setAccountNumberDestination(accountNumber);
		movement.setAmount(100.0);
		Flux<MovementsCollection> movements = Flux.just(movement);
		
		when(passiveAccountService.getMaximumTransactions(accountNumber)).thenReturn(Mono.just(maximumTransactions));
		when(repository.findAll()).thenReturn(movements);
		when(passiveAccountService.getDayMovementAvailable(accountNumber)).thenReturn(Mono.just(dayMovementAvailable));
		when(passiveAccountService.getAccountBalance(accountNumber)).thenReturn(Mono.just(amountBalance));
		when(passiveAccountService.getTransactionCommission(accountNumber)).thenReturn(Mono.just(transactionCommission));
		when(passiveAccountService.getMaximumTransactionsWithoutCommission(accountNumber)).thenReturn(Mono.just(maximumTransactionsWithoutCommission));
		when(repository.save(movement)).thenReturn(Mono.just(movement));
		when(passiveAccountService.updateAccountBalance(accountNumber,amountBalance)).thenReturn(Mono.empty());
		
		Mono<Void> expected = service.saveWithdrawal(movement);

		assertNotNull(expected);
	}
	
	@Test
	@DisplayName("Test save a transfer with same account")
	void saveTransferWithSameAccountTest() {
		
		String sourceAccountNumber = "123456";
		String destinationAccountNumber = "123789";
		Integer maximumTransactions = 10;
		Integer dayMovementAvailable = LocalDate.now().getDayOfMonth();
		Double amountBalance = 1000.0;
		Double transactionCommission = 10.0;
		Integer maximumTransactionsWithoutCommission = 10;
		
		MovementsCollection movement = new MovementsCollection();
		movement.setAccountNumberSource(sourceAccountNumber);
		movement.setAccountNumberDestination(destinationAccountNumber);
		movement.setAmount(100.0);
		Flux<MovementsCollection> movements = Flux.just(movement);
		
		when(passiveAccountService.getMaximumTransactions(sourceAccountNumber)).thenReturn(Mono.just(maximumTransactions));
		when(repository.findAll()).thenReturn(movements);
		when(passiveAccountService.getDayMovementAvailable(sourceAccountNumber)).thenReturn(Mono.just(dayMovementAvailable));
		when(passiveAccountService.getAccountBalance(sourceAccountNumber)).thenReturn(Mono.just(amountBalance));
		when(passiveAccountService.getTransactionCommission(sourceAccountNumber)).thenReturn(Mono.just(transactionCommission));
		when(passiveAccountService.getMaximumTransactionsWithoutCommission(sourceAccountNumber)).thenReturn(Mono.just(maximumTransactionsWithoutCommission));
		when(repository.save(movement)).thenReturn(Mono.just(movement));
		when(passiveAccountService.updateAccountBalance(sourceAccountNumber,amountBalance)).thenReturn(Mono.empty());
		
		Mono<MovementsCollection> expected = service.saveTransferWithSameAccount(movement);

		assertNotNull(expected);
		
		
	}
	
	@Test
	@DisplayName("Test save a transfer a third account")
	void saveTransferThirdAccountTest() {
		String sourceAccountNumber = "123456";
		String destinationAccountNumber = "123789";
		Integer maximumTransactions = 10;
		Integer dayMovementAvailable = LocalDate.now().getDayOfMonth();
		Double amountBalance = 1000.0;
		Double transactionCommission = 10.0;
		Integer maximumTransactionsWithoutCommission = 10;
		
		MovementsCollection movement = new MovementsCollection();
		movement.setAccountNumberSource(sourceAccountNumber);
		movement.setAccountNumberDestination(destinationAccountNumber);
		movement.setAmount(100.0);
		Flux<MovementsCollection> movements = Flux.just(movement);
		
		when(passiveAccountService.getMaximumTransactions(sourceAccountNumber)).thenReturn(Mono.just(maximumTransactions));
		when(repository.findAll()).thenReturn(movements);
		when(passiveAccountService.getDayMovementAvailable(sourceAccountNumber)).thenReturn(Mono.just(dayMovementAvailable));
		when(passiveAccountService.getAccountBalance(sourceAccountNumber)).thenReturn(Mono.just(amountBalance));
		when(passiveAccountService.getTransactionCommission(sourceAccountNumber)).thenReturn(Mono.just(transactionCommission));
		when(passiveAccountService.getMaximumTransactionsWithoutCommission(sourceAccountNumber)).thenReturn(Mono.just(maximumTransactionsWithoutCommission));
		when(repository.save(movement)).thenReturn(Mono.just(movement));
		when(passiveAccountService.updateAccountBalance(sourceAccountNumber,amountBalance)).thenReturn(Mono.empty());
		
		Mono<MovementsCollection> expected = service.saveTransferThirdAccount(movement);

		assertNotNull(expected);
	}
	
	@Test
	@DisplayName("Test save a withdrawal with debit card")
	void saveWithdrawalWithDebitCardTest() {
		
		String debitCardNumber = "123456";
		String sourceAccountNumber = "123456";
		String destinationAccountNumber = "123789";
		String personCode = "123456";
		Integer maximumTransactions = 10;
		Integer dayMovementAvailable = LocalDate.now().getDayOfMonth();
		Double amountBalance = 1000.0;
		Double transactionCommission = 10.0;
		Integer maximumTransactionsWithoutCommission = 10;
		
		MovementsCollection movement = new MovementsCollection();
		movement.setDebitCardNumber(debitCardNumber);
		movement.setPersonCode(personCode);
		movement.setAccountNumberDestination(destinationAccountNumber);
		movement.setAmount(100.0);
		Flux<MovementsCollection> movements = Flux.just(movement);
		
		when(debitCardService.getMainAccountNumber(debitCardNumber)).thenReturn(Mono.just(sourceAccountNumber));
		when(passiveAccountService.getAccountNumberAvailable(personCode, movement.getAmount())).thenReturn(Mono.just(sourceAccountNumber));
		
		when(passiveAccountService.getMaximumTransactions(sourceAccountNumber)).thenReturn(Mono.just(maximumTransactions));
		when(repository.findAll()).thenReturn(movements);
		when(passiveAccountService.getDayMovementAvailable(sourceAccountNumber)).thenReturn(Mono.just(dayMovementAvailable));
		when(passiveAccountService.getAccountBalance(sourceAccountNumber)).thenReturn(Mono.just(amountBalance));
		when(passiveAccountService.getTransactionCommission(sourceAccountNumber)).thenReturn(Mono.just(transactionCommission));
		when(passiveAccountService.getMaximumTransactionsWithoutCommission(sourceAccountNumber)).thenReturn(Mono.just(maximumTransactionsWithoutCommission));
		when(repository.save(movement)).thenReturn(Mono.just(movement));
		when(passiveAccountService.updateAccountBalance(sourceAccountNumber,amountBalance)).thenReturn(Mono.empty());
		
		Mono<MovementsCollection> expected = service.saveWithdrawalWithDebitCard(movement);

		assertNotNull(expected);
	}
	
	@Test
	@DisplayName("Test get movements of an account")
	void getByAccountNumberTest() {
		
		MovementsCollection movement = new MovementsCollection();
		movement.setAccountNumberDestination("123456");
		movement.setAmount(100.0);
		
		MovementsCollection movement2 = new MovementsCollection();
		movement2.setAccountNumberSource("123456");
		movement2.setAmount(100.0);
		
		when(repository.findAll()).thenReturn(Flux.just(movement,movement2));
		
		Flux<MovementsCollection> expected = service.getByAccountNumber("123456");
		assertNotNull(expected);
		
		
	}
	
	@Test
	@DisplayName("Test check if maximum transactions is allowed")
	void checkMaximumTransactionsTest() {
		
		String accountNumber = "123456";
		Integer maximumTransactions = 10;
		
		MovementsCollection movement = new MovementsCollection();
		movement.setAccountNumberDestination(accountNumber);
		movement.setAmount(100.0);
		Flux<MovementsCollection> movements = Flux.just(movement);
		
		when(passiveAccountService.getMaximumTransactions(accountNumber)).thenReturn(Mono.just(maximumTransactions));
		when(repository.findAll()).thenReturn(movements);
		
		Mono<Boolean> expect = service.checkMaximumTransactions(accountNumber);
		assertNotNull(expect);
		assertEquals(false, expect.block());
		
		
	}
	
	@Test
	@DisplayName("Test check if maximum transactions without commission is allowed")
	void checkMaximumTransactionsWithoutCommissionTest() {
		String accountNumber = "123456";
		Integer maximumTransactionsWithoutCommission = 10;
		
		MovementsCollection movement = new MovementsCollection();
		movement.setAccountNumberDestination(accountNumber);
		movement.setAmount(100.0);
		Flux<MovementsCollection> movements = Flux.just(movement);
		
		when(passiveAccountService.getMaximumTransactionsWithoutCommission(accountNumber)).thenReturn(Mono.just(maximumTransactionsWithoutCommission));
		when(repository.findAll()).thenReturn(movements);
		
		Mono<Boolean> expect = service.checkMaximumTransactionsWithoutCommission(accountNumber);
		assertNotNull(expect);
		assertEquals(false, expect.block());
	}
	
	@Test
	@DisplayName("Test check if day movement available is allowed")
	void checkDayMovementAvailableTest() {
		
		String accountNumber = "123456";
		Integer dayMovementAvailable = LocalDate.now().getDayOfMonth();
		
		when(passiveAccountService.getDayMovementAvailable(accountNumber)).thenReturn(Mono.just(dayMovementAvailable));
		
		Mono<Boolean> expected = service.checkDayMovementAvailable(accountNumber);
		assertNotNull(expected);
		assertEquals(true, expected.block());
		
	}
	
	@Test
	@DisplayName("Test check if have minimum average amount is allowed")
	void checkIfHaveAverageAmountTest() {
		
		/*String accountNumber = "123456";
		Double minimumAverageAmount = 200.0;
		
		LocalDate localDate = LocalDate.now(); 
		Date fechaMesActual = localDate.toDate();
		Date fechaMesAnterior = localDate.minusMonths(1).toDate();
		
		MovementsCollection movement = new MovementsCollection();
		movement.setAccountNumberDestination("123456");
		movement.setCreatedAt(fechaMesActual);
		movement.setAmount(1000.0);
		
		MovementsCollection movement2 = new MovementsCollection();
		movement2.setAccountNumberSource("123456");
		movement2.setCreatedAt(fechaMesAnterior);
		movement2.setAmount(200.0);
		
		when(repository.findAll()).thenReturn(Flux.just(movement,movement2));
		
		Mono<Boolean> expected = service.checkIfHaveAverageAmount(accountNumber, minimumAverageAmount);
		
		assertNotNull(expected);
		assertEquals(true, expected.block());*/
		
		
	}
	
}
