package com.nttdata.microservice.bankpassiveaccounts.facade.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import reactor.core.publisher.Mono;

class PassiveAccountFacadeImplTest {
	
	@InjectMocks
	private PassiveAccountFacadeImpl facade;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	@DisplayName("Test if have a credit debt")
	public void checkIfHaveDebtTest() {
		
		String personCode = "123456";
		
		Mono<Boolean> expected = facade.checkIfHaveDebt(personCode);
		
		assertNotNull(expected);
		assertEquals(false, expected.block());
	}

	@Test
	@DisplayName("Test if have a credit card")
	public void checkIfCreditCardTest() {
		
		String personCode = "123456";
		
		Mono<Boolean> expected = facade.checkIfCreditCard(personCode);
		
		assertNotNull(expected);
		assertEquals(true, expected.block());
	}
	

}
