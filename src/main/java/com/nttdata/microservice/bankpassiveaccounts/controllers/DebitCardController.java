package com.nttdata.microservice.bankpassiveaccounts.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nttdata.microservice.bankpassiveaccounts.collections.DebitCardCollection;
import com.nttdata.microservice.bankpassiveaccounts.services.IDebitCardService;


import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "debitcard")
public class DebitCardController {

	private static Logger logger = Logger.getLogger(DebitCardController.class);
	@Autowired
	private IDebitCardService service;
	
	@PostMapping(value = "/save")
	public Mono<DebitCardCollection> save(@RequestBody DebitCardCollection collection) throws Exception{
		logger.info("save debit card");
		return service.save(collection);
	}
	
	@PostMapping(value = "/updateMainAccountNumber/{debitCardNumber}/{accountNumber}")
	public Mono<DebitCardCollection> updateMainAccountNumber(@PathVariable("debitCardNumber") String debitCardNumber, @PathVariable("accountNumber") String accountNumber) throws Exception{
		logger.info("update main account number");
		return service.updateMainAccountNumber(debitCardNumber, accountNumber);
	}
	
	
}
