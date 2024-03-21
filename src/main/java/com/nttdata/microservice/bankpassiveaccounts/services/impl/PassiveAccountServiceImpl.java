package com.nttdata.microservice.bankpassiveaccounts.services.impl;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nttdata.microservice.bankpassiveaccounts.collections.Customer;
import com.nttdata.microservice.bankpassiveaccounts.collections.Movement;
import com.nttdata.microservice.bankpassiveaccounts.collections.PassiveAccountCollection;
import com.nttdata.microservice.bankpassiveaccounts.collections.enums.CreditStateEnum;
import com.nttdata.microservice.bankpassiveaccounts.collections.enums.CustomerStateEnum;
import com.nttdata.microservice.bankpassiveaccounts.collections.enums.CustomerTypeEnum;
import com.nttdata.microservice.bankpassiveaccounts.collections.enums.MovementTypeEnum;
import com.nttdata.microservice.bankpassiveaccounts.collections.enums.PassiveAccountStateEnum;
import com.nttdata.microservice.bankpassiveaccounts.collections.enums.PassiveAccountTypeEnum;
import com.nttdata.microservice.bankpassiveaccounts.dto.CreditDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.CustomerDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.MovementCommissionDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.MovementDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.PassiveAccountBalanceDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.PassiveAccountCreateDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.PassiveAccountDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.PassiveAccountMovementDto;
import com.nttdata.microservice.bankpassiveaccounts.exceptions.CustomerInactiveException;
import com.nttdata.microservice.bankpassiveaccounts.exceptions.PassiveAccountException;
import com.nttdata.microservice.bankpassiveaccounts.repository.IPassiveAccountRepository;
import com.nttdata.microservice.bankpassiveaccounts.services.IPassiveAccountService;
import com.nttdata.microservice.bankpassiveaccounts.utils.CustomerUtil;
import com.nttdata.microservice.bankpassiveaccounts.utils.MovementUtil;
import com.nttdata.microservice.bankpassiveaccounts.utils.PassiveAccountInfoUtil;
import com.nttdata.microservice.bankpassiveaccounts.utils.PassiveAccountUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class PassiveAccountServiceImpl implements IPassiveAccountService{
	
	@Autowired
	private IPassiveAccountRepository repository;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Autowired
	private PassiveAccountUtil passiveAccountUtil;
	
	@Autowired
	private PassiveAccountInfoUtil passiveAccounInfoUtil;
	
	@Autowired
	private CustomerUtil customerUtil;
	
	@Autowired
	private MovementUtil movementUtil;
	
	@Override
    public Mono<PassiveAccountCollection> create(PassiveAccountCreateDto passiveAccountCreateDto) {
        log.info("Start of operation to create an account");

        if (passiveAccountCreateDto.getCustomerId() == null || !passiveAccountCreateDto.getCustomerId().isBlank()) {
            Mono<PassiveAccountCollection> createdAccount = findCustomerById(passiveAccountCreateDto.getCustomerId())
                    .flatMap(retrievedCustomer -> {
                        log.info("Applying generic account validations");
                        return createGenericValidation(passiveAccountCreateDto, retrievedCustomer);
                    })
                    .flatMap(genericValidatedCustomer -> {
                        log.info("Applying customer group validations");
                        if (genericValidatedCustomer.getPersonType().contentEquals(CustomerTypeEnum.PERSONAL.toString())
                        		|| genericValidatedCustomer.getPersonType().contentEquals(CustomerTypeEnum.PERSONAL_VIP.toString())) {
                            return createPersonalValidation(passiveAccountCreateDto, genericValidatedCustomer);
                        } else if (genericValidatedCustomer.getPersonType().contentEquals(CustomerTypeEnum.ENTERPRISE.toString())
                        		|| genericValidatedCustomer.getPersonType().contentEquals(CustomerTypeEnum.ENTERPRISE_PYME.toString())) {
                            return createEnterpriseValidation(passiveAccountCreateDto, genericValidatedCustomer);
                        } else {
                            log.warn("Customer's type is not supported");
                            log.warn("Proceeding to abort create account");
                            return Mono.error(new PassiveAccountException("Not supported customer type"));
                        }
                    })
                    .flatMap(validatedCustomer -> {
                        PassiveAccountCollection accountToCreate = passiveAccountUtil.toPassiveAccountCollection(passiveAccountCreateDto);
                        Customer customer = customerUtil.toCustomer(validatedCustomer);

                        accountToCreate.setCustomer(customer);
                        accountToCreate.setState(PassiveAccountStateEnum.ACTIVE.toString());
                        accountToCreate.setCurrentBalance(0.0);

                        log.info("Creating new account: [{}]", accountToCreate.toString());
                        return repository.save(accountToCreate);
                    })
                    .switchIfEmpty(Mono.error(new NoSuchElementException("Customer does not exist")));

            log.info("End of operation to create an account");
            return createdAccount;
        } else {
            log.warn("Account does not contain a customer id");
            log.warn("Proceeding to abort create account");
            return Mono.error(new IllegalArgumentException("Account does not contain customer id"));
        }
    }
	
	@Override
    public Mono<CustomerDto> findCustomerById(String id) {
        log.info("Start of operation to retrieve customer with id [{}] from customer-service", id);

        log.info("Retrieving customer");
        String url = "http://bank-gateway:8090/person/getCustomer/" + id;
        log.info("url: " + url);
        Mono<CustomerDto> retrievedCustomer = webClientBuilder.build().get()
                        .uri(url)
                        .retrieve()
                        .onStatus(httpStatus -> httpStatus == HttpStatus.NOT_FOUND, clientResponse -> Mono.empty())
                        .bodyToMono(CustomerDto.class);
        log.info("Customer retrieved successfully");

        log.info("End of operation to retrieve customer with id: [{}]", id);
        return retrievedCustomer;
    }
	

    @Override
    public Mono<PassiveAccountCollection> findById(String id) {
        log.info("Start of operation to find an account by id");

        log.info("Retrieving account with id: [{}]", id);
        Mono<PassiveAccountCollection> retrievedAccount = repository.findById(new ObjectId(id));
        log.info("Account with id: [{}] was retrieved successfully", id);

        log.info("End of operation to find an account by id");
        return retrievedAccount;
    }

    @Override
    public Flux<PassiveAccountDto> findAll() {
        log.info("Start of operation to retrieve all accounts");

        log.info("Retrieving all accounts");
        Flux<PassiveAccountDto> retrievedAccount = repository.findAll().map(account -> {
        	return passiveAccountUtil.toPassiveAccountDto(account);
        });
        log.info("All accounts retrieved successfully");

        log.info("End of operation to retrieve all accounts");
        return retrievedAccount;
    }	
    
    private Mono<CustomerDto> createGenericValidation(PassiveAccountCreateDto accountToCreate, CustomerDto customerFromMicroservice) {
        log.info("Customer exists in database");

        if (customerFromMicroservice.getState().contentEquals(CustomerStateEnum.INACTIVE.toString()))
        {
            log.warn("Customer have blocked status");
            log.warn("Proceeding to abort create account");
            return Mono.error(new CustomerInactiveException("The customer have inactive status"));
        }

        

        return Mono.just(customerFromMicroservice);
    }

    private Mono<CustomerDto> createPersonalValidation(PassiveAccountCreateDto accountToCreate, CustomerDto customerFromMicroservice) {
        if (customerFromMicroservice.getPersonType().contentEquals(CustomerTypeEnum.PERSONAL.toString())
        		&& accountToCreate.getAccountType().contentEquals(PassiveAccountTypeEnum.SAVING_ACCOUNT_VIP.toString())) {
            log.warn("Standard customers can not create vip accounts");
            log.warn("Proceeding to abort create account");
            return Mono.error(new PassiveAccountException("Standard customer can not create vip accounts"));
        }

        return findByCustomerId(customerFromMicroservice.getId())
                .filter(retrievedAccount -> retrievedAccount.getState().contentEquals(CustomerStateEnum.ACTIVE.toString()))
                .hasElements()
                .flatMap(haveAnAccount -> {
                    if (Boolean.TRUE.equals(haveAnAccount)) {
                        log.warn("Can not create more than one account for a personal customer");
                        log.warn("Proceeding to abort create account");
                        return Mono.error(new PassiveAccountException("Customer have more than one account"));
                    }

                    if (accountToCreate.getAccountType().contentEquals(PassiveAccountTypeEnum.SAVING_ACCOUNT_VIP.toString())) {
                        return customerHaveCreditValidation(customerFromMicroservice);
                    } else {
                        log.info("Account successfully validated");
                        return Mono.just(customerFromMicroservice);
                    }
                });
    }

    private Mono<CustomerDto> createEnterpriseValidation(PassiveAccountCreateDto accountToCreate, CustomerDto customerFromMicroservice) {
    	
    	if (accountToCreate.getHolders() == null || accountToCreate.getHolders().isEmpty()) {
            log.warn("Account does not contain holders, must have at least one");
            log.warn("Proceeding to abort create account");
            return Mono.error(new IllegalArgumentException("Account does not contain holders"));
        }
    	
        if (!accountToCreate.getAccountType().contentEquals(PassiveAccountTypeEnum.CURRENT_ACCOUNT.toString())
        		&& !accountToCreate.getAccountType().contentEquals(PassiveAccountTypeEnum.CURRENT_ACCOUNT_PYME.toString()))
        {
            log.warn("Can not create {} account for business customers. Accounts must be of current type", accountToCreate.getAccountType());
            log.warn("Proceeding to abort create account");
            return Mono.error(new PassiveAccountException("Account is not of current type"));
        }

        if (customerFromMicroservice.getPersonType().contentEquals(CustomerTypeEnum.ENTERPRISE.toString()) &&
                accountToCreate.getAccountType().contentEquals(PassiveAccountTypeEnum.CURRENT_ACCOUNT_PYME.toString())) {
            log.warn("Standard customers can not create pyme accounts");
            log.warn("Proceeding to abort create account");
            return Mono.error(new PassiveAccountException("Standard customer can not create pyme accounts"));
        }

        if (accountToCreate.getAccountType().contentEquals(PassiveAccountTypeEnum.CURRENT_ACCOUNT_PYME.toString())) {
            return customerHaveCreditValidation(customerFromMicroservice);
        } else {
            log.info("Account successfully validated");
            return Mono.just(customerFromMicroservice);
        }
    }

	@Override
	public Flux<PassiveAccountCollection> findByCustomerId(String id) {
		log.info("Start of operation to retrieve all accounts of the customer with id: [{}]", id);

        log.info("Retrieving accounts");
        Flux<PassiveAccountCollection> retrievedAccount = repository.findAccountsByCustomerId(id);
        log.info("Accounts retrieved successfully");

        log.info("End of operation to retrieve accounts of the customer with id: [{}]", id);
        return retrievedAccount;
	}
	
	private Mono<CustomerDto> customerHaveCreditValidation(CustomerDto customer) {
        return findCreditsByCustomerId(customer.getId())
                .filter(retrievedCredit -> retrievedCredit.getState().contentEquals(CreditStateEnum.ACTIVE.toString()))
                .hasElements()
                .flatMap(haveACredit -> {
                    if (Boolean.FALSE.equals(haveACredit)) {
                        log.warn("Can not create account because customer does not have a credit product");
                        log.warn("Proceeding to abort create account");
                        return Mono.error(new PassiveAccountException("Customer does not have a credit product"));
                    }

                    log.info("Account successfully validated");
                    return Mono.just(customer);
                });
    }
	
	@Override
    public Flux<CreditDto> findCreditsByCustomerId(String customerId) {
        log.info("Start of operation to retrieve credits of customer with id [{}] from active-operation-service", customerId);

        log.info("Retrieving credits");
        String url = "http://bank-gateway:8090/credit/findCreditsByCustomerId/" + customerId ;
        Flux<CreditDto> retrievedCredits = webClientBuilder.build().get()
						                        .uri(url)
						                        .retrieve()
						                        .onStatus(httpStatus -> httpStatus == HttpStatus.NOT_FOUND, clientResponse -> Mono.empty())
						                        .bodyToFlux(CreditDto.class);
        log.info("Customer retrieved successfully");

        log.info("End of operation to retrieve customer with id: [{}]", customerId);
        return retrievedCredits;
    }

	@Override
	public Mono<PassiveAccountCollection> doMovement(PassiveAccountMovementDto accountDto) {
		log.info("Start to save a new account operation for the account with id: [{}]", accountDto.getId());

        Mono<PassiveAccountCollection> updatedAccount = repository.findById(new ObjectId(accountDto.getId()))
                        .flatMap(retrievedAccount -> {
                            log.info("Validating operation");
                            return movementValidation(accountDto, retrievedAccount);
                        })
                        .flatMap(validatedAccount -> storeMovementIntoAccount(accountDto, validatedAccount))
                        .flatMap(transformedAccount -> {
                            if (accountDto.getMovement().getType().contentEquals(MovementTypeEnum.TRANSFER_OUT.toString())) {
                                // Creates the operation for the transfer in
                            	PassiveAccountMovementDto targetAccountOperation = PassiveAccountMovementDto.builder()
                                                .id(accountDto.getTargetId())
                                                .movement(MovementDto.builder()
                                                        .amount(accountDto.getMovement().getAmount())
                                                        .type(MovementTypeEnum.TRANSFER_IN.toString())
                                                        .build())
                                                .build();
                                log.info("Sending operation to receiver account with id: [{}]", accountDto.getTargetId());
                                return doMovement(targetAccountOperation)
                                        .flatMap(transferInAccount -> repository.save(transformedAccount));
                            } else {
                                log.info("Saving operation into account: [{}]", transformedAccount.toString());
                                return repository.save(transformedAccount);
                            }
                        })
                        .switchIfEmpty(Mono.error(new NoSuchElementException("Account does not exist")));

        log.info("End to save a new account operation for the account with id: [{}]", accountDto.getId());
        return updatedAccount;
	}
	
	private Mono<PassiveAccountCollection> movementValidation(PassiveAccountMovementDto accountToUpdateOperation, PassiveAccountCollection accountInDatabase) {
        log.info("Account exists in database");

        if (accountInDatabase.getState().contentEquals(PassiveAccountStateEnum.INACTIVE.toString())) {
            log.warn("Account have blocked status");
            log.warn("Proceeding to abort do operation");
            return Mono.error(new CustomerInactiveException("The account have blocked status"));
        }

        if (accountToUpdateOperation.getMovement().getType().contentEquals(MovementTypeEnum.WITHDRAWAL.toString()) ||
                accountToUpdateOperation.getMovement().getType().contentEquals(MovementTypeEnum.TRANSFER_OUT.toString())) {

            Double amountToTake = accountToUpdateOperation.getMovement().getAmount();
            if (accountInDatabase.getAccountInfo() != null &&
                    accountInDatabase.getAccountInfo().getOperationCommissionPercentage() != null &&
                    accountInDatabase.getAccountInfo().getMaximumNumberOfOperations() != null &&
                    accountInDatabase.getDoneOperationsInMonth() != null &&
                    accountInDatabase.getAccountInfo().getMaximumNumberOfOperations() <= accountInDatabase.getDoneOperationsInMonth()) {
                amountToTake = passiveAccounInfoUtil.roundDouble(passiveAccounInfoUtil.applyInterests(amountToTake, accountInDatabase.getAccountInfo().getOperationCommissionPercentage()), 2);
            }

            if (accountInDatabase.getCurrentBalance() < amountToTake) {
                log.info("Account has insufficient funds");
                log.warn("Proceeding to abort do operation");
                return Mono.error(new IllegalArgumentException("The account has insufficient funds"));
            }
        }

        if (accountInDatabase.getAccountType().contentEquals(PassiveAccountTypeEnum.FIX_TERM_ACCOUNT.toString()) &&
            !accountInDatabase.getAccountInfo().getAllowedDayForOperations().equals(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))) {
            log.info("Allowed day [{}] for operations in this account does not match with current day of the month [{}]",
                    accountInDatabase.getAccountInfo().getAllowedDayForOperations(),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            log.warn("Proceeding to abort do operation");
            return Mono.error(new PassiveAccountException("Allowed day for operations in this account does not match with current day of the month"));
        }

        log.info("Operation successfully validated");
        return Mono.just(accountInDatabase);
    }
	
	private Mono<PassiveAccountCollection> storeMovementIntoAccount(PassiveAccountMovementDto accountDTO, PassiveAccountCollection accountInDatabase) {
        Double commission = 0.0;
        if (!accountDTO.getMovement().getType().contentEquals(MovementTypeEnum.TRANSFER_OUT.toString())) {
            // Increments the number of done operations
            accountInDatabase.setDoneOperationsInMonth(accountInDatabase.getDoneOperationsInMonth() == null ? 1 : accountInDatabase.getDoneOperationsInMonth() + 1);

            // Calculates the commission
            if (accountInDatabase.getAccountInfo() != null &&
                    accountInDatabase.getAccountInfo().getMaximumNumberOfOperations() != null &&
                    accountInDatabase.getDoneOperationsInMonth() > accountInDatabase.getAccountInfo().getMaximumNumberOfOperations())
            {
                commission = passiveAccounInfoUtil.roundDouble(passiveAccounInfoUtil.calculateCommission(accountDTO.getMovement().getAmount(), accountInDatabase.getAccountInfo().getOperationCommissionPercentage()), 2);
            }
        }

        // Calculates the new current balance
        Double newCurrentBalance = passiveAccounInfoUtil.roundDouble((accountDTO.getMovement().getType().contentEquals(MovementTypeEnum.DEPOSIT.toString()) ||
                accountDTO.getMovement().getType().contentEquals(MovementTypeEnum.TRANSFER_IN.toString())) ?
                accountInDatabase.getCurrentBalance() + accountDTO.getMovement().getAmount() - commission :
                accountInDatabase.getCurrentBalance() - accountDTO.getMovement().getAmount() - commission, 2);
        accountInDatabase.setCurrentBalance(newCurrentBalance);

        // Creates the new operation
        Movement movement = movementUtil.toMovement(accountDTO.getMovement());
        movement.setTime(new Date());
        movement.setCommission(commission);
        movement.setFinalBalance(newCurrentBalance);

        ArrayList<Movement> movements = accountInDatabase.getMovements() == null ? new ArrayList<>() : accountInDatabase.getMovements();
        movements.add(movement);

        accountInDatabase.setMovements(movements);

        return Mono.just(accountInDatabase);
    }
	
	@Override
    public Flux<Movement> findOperationsByAccountId(String id) {
        log.info("Start of operation to retrieve all operations from account with id: [{}]", id);

        log.info("Retrieving all operations");
        Flux<Movement> retrievedOperations = findById(id)
                .filter(retrievedAccount -> retrievedAccount.getMovements() != null)
                .flux()
                .flatMap(retrievedAccount -> Flux.fromIterable(retrievedAccount.getMovements()));
        log.info("Operations retrieved successfully");

        log.info("End of operation to retrieve operations from account with id: [{}]", id);
        return retrievedOperations;
    }
	
	@Override
    public Flux<PassiveAccountBalanceDto> findBalancesByCustomerId(String id) {
        log.info("Start of operation to retrieve account balances from customer with id: [{}]", id);

        log.info("Retrieving account balances");
        Flux<PassiveAccountBalanceDto> retrievedBalances = findByCustomerId(id)
                .map(passiveAccountUtil::toPassiveAccountBalanceDto);
        log.info("Balances retrieved successfully");

        log.info("End of operation to retrieve account balances from customer with id: [{}]", id);
        return retrievedBalances;
    }
	
	@Override
    public Flux<MovementCommissionDto> findCommissionsBetweenDatesByAccountId(Date dateFrom, Date dateTo, String id) {
        log.info("Start of operation to retrieve all commissions from account with id: [{}] from [] to []", id, dateFrom, dateTo);

        log.info("Retrieving all commissions");
        Flux<MovementCommissionDto> retrievedOperations = findById(id)
                .filter(retrievedAccount -> retrievedAccount.getMovements() != null)
                .flux()
                .flatMap(retrievedAccount -> Flux.fromIterable(retrievedAccount.getMovements()))
                .filter(retrievedOperation -> retrievedOperation.getCommission() != null &&
                                                retrievedOperation.getCommission() > 0 &&
                                                retrievedOperation.getTime().after(dateFrom) &&
                                                retrievedOperation.getTime().before(dateTo))
                .map(movementUtil::toMovementCommissionDto);
        log.info("Commissions retrieved successfully");

        log.info("End of operation to retrieve commissions from account with id: [{}]", id);
        return retrievedOperations;
    }
	
	
    

}
