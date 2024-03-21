package com.nttdata.microservice.bankpassiveaccounts.services;

import java.util.Date;

import com.nttdata.microservice.bankpassiveaccounts.collections.Movement;
import com.nttdata.microservice.bankpassiveaccounts.collections.PassiveAccountCollection;
import com.nttdata.microservice.bankpassiveaccounts.dto.CreditDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.CustomerDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.MovementCommissionDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.PassiveAccountBalanceDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.PassiveAccountCreateDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.PassiveAccountDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.PassiveAccountMovementDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPassiveAccountService {
	
	Mono<PassiveAccountCollection> create(PassiveAccountCreateDto PassiveAccountCreateDto);
    Mono<PassiveAccountCollection> findById(String id);
    Flux<PassiveAccountDto> findAll();
    Mono<CustomerDto> findCustomerById(String id);
    Flux<PassiveAccountCollection> findByCustomerId(String id);
    Flux<CreditDto> findCreditsByCustomerId(String customerId);
    
    Mono<PassiveAccountCollection> doMovement(PassiveAccountMovementDto accountDTO);
    
    Flux<MovementCommissionDto> findCommissionsBetweenDatesByAccountId(Date dateFrom, Date dateTo, String id);
    Flux<Movement> findOperationsByAccountId(String id);
    Flux<PassiveAccountBalanceDto> findBalancesByCustomerId(String id);
    
}
