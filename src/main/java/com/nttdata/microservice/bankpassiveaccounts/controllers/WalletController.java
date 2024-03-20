package com.nttdata.microservice.bankpassiveaccounts.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.microservice.bankpassiveaccounts.collections.DebitCardCollection;
import com.nttdata.microservice.bankpassiveaccounts.collections.WalletCollection;
import com.nttdata.microservice.bankpassiveaccounts.services.IWalletService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "wallet")
public class WalletController {
	
	private static Logger logger = Logger.getLogger(WalletController.class);
	@Autowired
	private IWalletService service;
	
	@PostMapping(value = "/save")
	public Mono<WalletCollection> save(@RequestBody WalletCollection collection) throws Exception{
		logger.info("save wallet");
		return service.save(collection);
	}
	
	@PostMapping(value = "/updateDebitCardNumber/{walletNumber}/{debitCardNumber}")
	public Mono<WalletCollection> updateDebitCardNumber(@PathVariable("walletNumber") String walletNumber, @PathVariable("debitCardNumber") String debitCardNumber) throws Exception{
		logger.info("update main account number");
		return service.updateDebitCardNumber(walletNumber, debitCardNumber);
	}
	

}
