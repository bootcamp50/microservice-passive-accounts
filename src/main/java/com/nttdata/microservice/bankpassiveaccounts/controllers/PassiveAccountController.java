package com.nttdata.microservice.bankpassiveaccounts.controllers;

import java.util.Date;
import java.util.NoSuchElementException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nttdata.microservice.bankpassiveaccounts.collections.Movement;
import com.nttdata.microservice.bankpassiveaccounts.collections.PassiveAccountCollection;
import com.nttdata.microservice.bankpassiveaccounts.dto.MovementCommissionDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.PassiveAccountBalanceDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.PassiveAccountCreateDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.PassiveAccountDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.PassiveAccountMovementDto;
import com.nttdata.microservice.bankpassiveaccounts.exceptions.CustomerInactiveException;
import com.nttdata.microservice.bankpassiveaccounts.exceptions.PassiveAccountException;
import com.nttdata.microservice.bankpassiveaccounts.services.IPassiveAccountService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping(value = "passive-accounts")
public class PassiveAccountController {
	
	//private static Logger logger = Logger.getLogger(PassiveAccountController.class);

	@Autowired
	private IPassiveAccountService pasiveAccountService;
	
	@GetMapping("/accounts")
    public Flux<PassiveAccountDto> findAllAccounts(){
        log.info("Get All pasive accounts");
        return pasiveAccountService.findAll();
    }
	
	@GetMapping("findAccountsByCustomerId/{id}")
    public Flux<PassiveAccountCollection> findAccountsByCustomerId(@PathVariable("id") String id) {
        log.info("Get operation in /customers/{}/accounts", id);
        return pasiveAccountService.findByCustomerId(id);
    }
	
	@PostMapping("/saveAccount")
    public Mono<ResponseEntity<PassiveAccountCollection>> createAccount(@RequestBody PassiveAccountCreateDto accountDTO) {
        log.info("Post operation in /accounts");
        return pasiveAccountService.create(accountDTO)
                .flatMap(createdAccount -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(createdAccount)))
                .onErrorResume(CustomerInactiveException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.LOCKED).build()))
                .onErrorResume(PassiveAccountException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()))
                .onErrorResume(IllegalArgumentException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()))
                .onErrorResume(NoSuchElementException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .onErrorResume(NullPointerException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build()))
                //.onErrorResume(CircuitBreakerException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build()))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(null)));
    }
	
	@PostMapping("/saveMovement")
    public Mono<ResponseEntity<PassiveAccountCollection>> saveMovement(@RequestBody PassiveAccountMovementDto accountDto) {
        log.info("Post operation in /accounts/operation");
        return pasiveAccountService.doMovement(accountDto)
                .flatMap(createdAccount -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(createdAccount)))
                .onErrorResume(CustomerInactiveException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.LOCKED).build()))
                .onErrorResume(PassiveAccountException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()))
                .onErrorResume(IllegalArgumentException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()))
                .onErrorResume(NoSuchElementException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(null)));
    }
	
	@GetMapping("/findMovementsByAccountId/{id}")
    public Flux<Movement> findMovementsByAccountId(@PathVariable("id") String id) {
        log.info("findMovementsByAccountId/{}", id);
        return pasiveAccountService.findOperationsByAccountId(id);
    }

    @GetMapping("/findCommissionsBetweenDatesByAccountId/{id}")
    public Flux<MovementCommissionDto> findCommissionsBetweenDatesByAccountId(@RequestParam(value = "date-from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFrom,
                                                                                  @RequestParam(value = "date-to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateTo,
                                                                                  @PathVariable("id") String id) {
        log.info("findCommissionsBetweenDatesByAccountId/{}", id);
        return pasiveAccountService.findCommissionsBetweenDatesByAccountId(dateFrom, dateTo, id);
    }

    @GetMapping("findBalancesByCustomerId/{id}")
    public Flux<PassiveAccountBalanceDto> findBalancesByCustomerId(@PathVariable("id") String id) {
        log.info("findBalancesByCustomerId/{}", id);
        return pasiveAccountService.findBalancesByCustomerId(id);
    }
    
}
