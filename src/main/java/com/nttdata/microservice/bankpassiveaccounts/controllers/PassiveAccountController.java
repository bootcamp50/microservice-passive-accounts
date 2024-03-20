package com.nttdata.microservice.bankpassiveaccounts.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.microservice.bankpassiveaccounts.collections.PassiveAccountCollection;
import com.nttdata.microservice.bankpassiveaccounts.dto.CurrentAccountEnterpriseDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.CurrentAccountPersonalDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.FixTermPersonalDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.SavingAccountPersonalDto;
import com.nttdata.microservice.bankpassiveaccounts.services.IPassiveAccountService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "passive-accounts")
public class PassiveAccountController {
	
	private static Logger logger = Logger.getLogger(PassiveAccountController.class);

	@Autowired
	private IPassiveAccountService pasiveAccountService;
	
	@PostMapping(value = "/saveCurrentPersonalAccount")
	public Mono<PassiveAccountCollection> saveCurrentPersonalAccount(@RequestBody CurrentAccountPersonalDto dto) throws Exception{
		logger.info("save passive account");
		PassiveAccountCollection passiveAccountCollection = new PassiveAccountCollection();
		passiveAccountCollection.setAccountNumber(dto.getAccountNumber());
		passiveAccountCollection.setAccountAmount(dto.getAccountAmount());
		passiveAccountCollection.setAccountBalance(dto.getAccountBalance());
		passiveAccountCollection.setPersonCode(dto.getPersonCode());
		passiveAccountCollection.setMaintenanceCommission(dto.getMaintenanceCommission());
		passiveAccountCollection.setTransactionCommission(dto.getTransactionCommission());
		passiveAccountCollection.setMaximumTransactionsWithoutCommission(dto.getMaximumTransactionsWithoutCommission());
		return pasiveAccountService.saveCurrentPersonalAccount(passiveAccountCollection);
	}
	
	@PostMapping(value = "/saveSavingPersonalAccount")
	public Mono<PassiveAccountCollection> saveSavingPersonalAccount(@RequestBody SavingAccountPersonalDto dto) throws Exception{
		logger.info("save passive account");
		PassiveAccountCollection passiveAccountCollection = new PassiveAccountCollection();
		passiveAccountCollection.setAccountNumber(dto.getAccountNumber());
		passiveAccountCollection.setAccountAmount(dto.getAccountAmount());
		passiveAccountCollection.setAccountBalance(dto.getAccountBalance());
		passiveAccountCollection.setPersonCode(dto.getPersonCode());
		passiveAccountCollection.setMaximumTransactions(dto.getMaximumTransactions());
		passiveAccountCollection.setTransactionCommission(dto.getTransactionCommission());
		return pasiveAccountService.saveSavingPersonalAccount(passiveAccountCollection);
	}
	
	@PostMapping(value = "/saveFixTermPersonalAccount")
	public Mono<PassiveAccountCollection> saveFixTermPersonalAccount(@RequestBody FixTermPersonalDto dto) throws Exception{
		logger.info("save passive account");
		PassiveAccountCollection passiveAccountCollection = new PassiveAccountCollection();
		passiveAccountCollection.setAccountNumber(dto.getAccountNumber());
		passiveAccountCollection.setAccountAmount(dto.getAccountAmount());
		passiveAccountCollection.setAccountBalance(dto.getAccountBalance());
		passiveAccountCollection.setMaximumTransactions(dto.getMaximumTransactions());
		passiveAccountCollection.setPersonCode(dto.getPersonCode());
		passiveAccountCollection.setDayMovementAvailable(dto.getDayMovementAvailable());
		passiveAccountCollection.setTransactionCommission(dto.getTransactionCommission());
		passiveAccountCollection.setMaximumTransactionsWithoutCommission(dto.getMaximumTransactionsWithoutCommission());
		
		return pasiveAccountService.saveFixTermPersonalAccount(passiveAccountCollection);
	}
	
	@PostMapping(value = "/saveCurrentEnterpriseAccount")
	public Mono<PassiveAccountCollection> saveCurrentEnterpriseAccount(@RequestBody CurrentAccountEnterpriseDto dto) throws Exception{
		logger.info("save passive account");
		PassiveAccountCollection passiveAccountCollection = new PassiveAccountCollection();
		passiveAccountCollection.setAccountNumber(dto.getAccountNumber());
		passiveAccountCollection.setPersonCode(dto.getPersonCode());
		passiveAccountCollection.setAccountAmount(dto.getAccountAmount());
		passiveAccountCollection.setAccountBalance(dto.getAccountBalance());
		passiveAccountCollection.setTransactionCommission(dto.getTransactionCommission());
		passiveAccountCollection.setMaximumTransactionsWithoutCommission(dto.getMaximumTransactionsWithoutCommission());
		
		return pasiveAccountService.saveCurrentEnterpriseAccount(passiveAccountCollection);
	}
	
	@PostMapping(value = "/saveVipPersonalAccount/{accountNumber}/{minimumAverageAmount}")
	public Mono<PassiveAccountCollection> saveVipPersonalAccount(@PathVariable("accountNumber") String accountNumber, @PathVariable("minimumAverageAmount") Double minimumAverageAccount) throws Exception{
		logger.info("save passive account");
		return pasiveAccountService.saveVipPersonalAccount(accountNumber, minimumAverageAccount);
	}
	
	@PostMapping(value = "/savePymeEnterpriseAccount/{accountNumber}")
	public Mono<PassiveAccountCollection> savePymeEnterpriseAccount(@PathVariable("accountNumber") String accountNumber) throws Exception{
		logger.info("save passive account");
		return pasiveAccountService.savePymeEnterpriseAccount(accountNumber);
	}
	
	
	// EXTERNAL VALIDATORS
	@GetMapping("/getAccountBalance/{accountNumber}")
	public Mono<Double> getAccountBalance(@PathVariable("accountNumber") String accountNumber)
			throws Exception {
		logger.info("get account balance by account number");
		return pasiveAccountService.getAccountBalance(accountNumber);
	}
	
	@GetMapping("/checkIfExist/{accountNumber}")
	public Mono<Boolean> checkIfExist(@PathVariable("accountNumber") String accountNumber)
			throws Exception {
		logger.info("check if account number exist");
		return pasiveAccountService.checkIfExist(accountNumber);
	}
	
	@PostMapping(value = "/updateCreditDebitNumber/{accountNumber}/{debitCardNumber}")
	public Mono<PassiveAccountCollection> updateCreditDebitNumber(@PathVariable("accountNumber") String accountNumber
			, @PathVariable("debitCardNumber") String debitCardNumber) throws Exception{
		logger.info("associate passive account with debit card");
		return pasiveAccountService.updateDebitCardNumber(accountNumber, debitCardNumber);
	}
	
}
