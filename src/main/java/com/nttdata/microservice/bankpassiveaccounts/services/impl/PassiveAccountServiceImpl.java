package com.nttdata.microservice.bankpassiveaccounts.services.impl;


import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.microservice.bankpassiveaccounts.collections.PassiveAccountCollection;
import com.nttdata.microservice.bankpassiveaccounts.collections.enums.PassiveAccountTypeEnum;
import com.nttdata.microservice.bankpassiveaccounts.collections.enums.PersonTypeEnum;
import com.nttdata.microservice.bankpassiveaccounts.facade.IPassiveAccountFacade;
import com.nttdata.microservice.bankpassiveaccounts.repository.IPassiveAccountRepository;
import com.nttdata.microservice.bankpassiveaccounts.services.IMovementService;
import com.nttdata.microservice.bankpassiveaccounts.services.IPassiveAccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PassiveAccountServiceImpl implements IPassiveAccountService{
	
	@Autowired
	private IPassiveAccountRepository repository;
	
	@Autowired
	private IPassiveAccountFacade facade;
	
	//@Autowired
	//private IMovementService movementService;
	

	@Override
	public Mono<PassiveAccountCollection> saveCurrentPersonalAccount(PassiveAccountCollection collection)
			{
		
		return getMinimumOpeningAmount(PassiveAccountTypeEnum.CURRENT_ACCOUNT.toString())
				.flatMap( minimumOpeningAmount -> {
					
					//SETTING DEFAULT VALUES
					collection.setAccountType(PassiveAccountTypeEnum.CURRENT_ACCOUNT.toString());
					collection.setPersonType(PersonTypeEnum.PERSONAL.toString());
					collection.setAccountNumber(UUID.randomUUID().toString());
					collection.setMaximumTransactions(0);
					
					collection.setCreatedAt(new Date());
					
					//VALIDATE MINIMUM OPENING AMOUNT
					if(Double.compare(minimumOpeningAmount, collection.getAccountAmount()) > 0 ) {
						return Mono.error(Exception::new);
					}
					
					return checkIfHaveMoreThanOneAccount(collection.getPersonCode()).flatMap(y -> {
						
						//CHECK IF HAVE MORE THAN ONE ACCOUNT
						if(Boolean.compare(true, y ) == 0) {
							return Mono.error(Exception::new);
						}
						
						return facade.checkIfHaveDebt(collection.getPersonCode()).flatMap(z -> {
							
							//CHECK IF HAVE DEBT
							if(Boolean.compare(true, z )==0) {
								return Mono.error(Exception::new);	
							}
							
							//SAVE ACCOUNT
							return repository.save(collection);
						});
						
						
					});
						
				});
		
	}

	@Override
	public Mono<PassiveAccountCollection> saveSavingPersonalAccount(PassiveAccountCollection collection)
			{
		
		return getMinimumOpeningAmount(PassiveAccountTypeEnum.SAVING_ACCOUNT.toString())
				.flatMap( minimumOpeningAmount -> {
					
					//SETTING DEFAULT VALUES
					collection.setAccountType(PassiveAccountTypeEnum.SAVING_ACCOUNT.toString());
					collection.setPersonType(PersonTypeEnum.PERSONAL.toString());
					collection.setAccountNumber(UUID.randomUUID().toString());
					collection.setCreatedAt(new Date());
					
					//VALIDATE MINIMUM OPENING AMOUNT
					if(Double.compare(minimumOpeningAmount, collection.getAccountAmount()) > 0 ) {
						return Mono.error(Exception::new);
					}
					
					return checkIfHaveMoreThanOneAccount(collection.getPersonCode()).flatMap(y -> {
						
						//CHECK IF HAVE MORE THAN ONE ACCOUNT
						if(Boolean.compare(true, y ) == 0) {
							return Mono.error(Exception::new);
						}
						
						return facade.checkIfHaveDebt(collection.getPersonCode()).flatMap(z -> {
							
							//CHECK IF HAVE DEBT
							if(Boolean.compare(true, z )==0) {
								return Mono.error(Exception::new);	
							}
							
							//SAVE ACCOUNT
							return repository.save(collection);
						});
					});
						
				});
	}

	@Override
	public Mono<PassiveAccountCollection> saveFixTermPersonalAccount(PassiveAccountCollection collection)
			{
		return getMinimumOpeningAmount(PassiveAccountTypeEnum.FIX_TERM_ACCOUNT.toString())
				.flatMap( minimumOpeningAmount -> {
					
					//SETTING DEFAULT VALUES
					collection.setAccountType(PassiveAccountTypeEnum.FIX_TERM_ACCOUNT.toString());
					collection.setPersonType(PersonTypeEnum.PERSONAL.toString());
					collection.setAccountNumber(UUID.randomUUID().toString());
					collection.setCreatedAt(new Date());
					
					//VALIDATE MINIMUM OPENING AMOUNT
					if(Double.compare(minimumOpeningAmount, collection.getAccountAmount()) > 0 ) {
						return Mono.error(Exception::new);
					}
					
					return checkIfHaveMoreThanOneAccount(collection.getPersonCode()).flatMap(y -> {
						
						//CHECK IF HAVE MORE THAN ONE ACCOUNT
						if(Boolean.compare(true, y ) == 0) {
							return Mono.error(Exception::new);
						}
						
						return facade.checkIfHaveDebt(collection.getPersonCode()).flatMap(z -> {
							
							//CHECK IF HAVE DEBT
							if(Boolean.compare(true, z )==0) {
								return Mono.error(Exception::new);	
							}
							
							//SAVE ACCOUNT
							return repository.save(collection);
						});
					});
						
				});
	}

	@Override
	public Mono<PassiveAccountCollection> saveCurrentEnterpriseAccount(PassiveAccountCollection collection)
			{
		return getMinimumOpeningAmount(PassiveAccountTypeEnum.CURRENT_ACCOUNT.toString())
				.flatMap( minimumOpeningAmount -> {
					
					//SETTING DEFAULT VALUES
					collection.setAccountType(PassiveAccountTypeEnum.CURRENT_ACCOUNT.toString());
					collection.setPersonType(PersonTypeEnum.ENTERPRISE.toString());
					collection.setAccountNumber(UUID.randomUUID().toString());
					collection.setCreatedAt(new Date());
					
					//VALIDATE MINIMUM OPENING AMOUNT
					if(Double.compare(minimumOpeningAmount, collection.getAccountAmount()) > 0 ) {
						return Mono.error(Exception::new);
					}
					
					return facade.checkIfHaveDebt(collection.getPersonCode()).flatMap(z -> {
						
						//CHECK IF HAVE DEBT
						if(Boolean.compare(true, z )==0) {
							return Mono.error(Exception::new);	
						}
						
						//SAVE ACCOUNT
						return repository.save(collection);
					});
						
				});
	}

	@Override
	public Mono<PassiveAccountCollection> saveVipPersonalAccount(String numberAccount, Double minimumAverageAmount){
		
		//GET SAVING ACCOUNT
		return repository.findByAccountNumber(numberAccount)
				.next()
				.flatMap(account -> {
					//VALIDATE MINIMUM OPENING AMOUNT
					/*return movementService.checkIfHaveAverageAmount(numberAccount, minimumAverageAmount)
							.flatMap(result -> {
								if(result == false) {
									return Mono.error(RuntimeException::new);
								}*/
								//CHECK IF HAVE CREDIT CARD
								return facade.checkIfCreditCard(account.getPersonCode())
										.flatMap(a -> {
										
											if(Boolean.compare(false, a )==0) {
											return Mono.error(RuntimeException::new);	
										}
										
										//CHECK IF HAVE DEBT
										return facade.checkIfHaveDebt(account.getPersonCode()).flatMap(z -> {
											if(Boolean.compare(true, z )==0) {
												return Mono.error(Exception::new);	
											}
											
											//SETTING DEFAULT VALUES
											account.setAccountType(PassiveAccountTypeEnum.VIP.toString());
											account.setUpdatedAt(new Date());
											account.setMinimumAverageAmount(minimumAverageAmount);
											
											//SAVE ACCOUNT
											return repository.save(account);
											
										});
									
								});
							//});
				});
		
	}

	@Override
	public Mono<PassiveAccountCollection> savePymeEnterpriseAccount(String accountNumber)
			 {
		//GET SAVING ACCOUNT
		return repository.findByAccountNumber(accountNumber)
				.next()
				.flatMap(account -> {

					//CHECK IF HAVE CREDIT CARD
					return facade.checkIfCreditCard(account.getPersonCode())
							.flatMap(a -> {
							
								if(Boolean.compare(false, a )==0) {
								return Mono.error(RuntimeException::new);	
							}
							
							//CHECK IF HAVE DEBT
							return facade.checkIfHaveDebt(account.getPersonCode()).flatMap(z -> {
								if(Boolean.compare(true, z )==0) {
									return Mono.error(Exception::new);	
								}
								
								//SETTING DEFAULT VALUES
								account.setAccountType(PassiveAccountTypeEnum.PYME.toString());
								account.setUpdatedAt(new Date());
								
								//SAVE ACCOUNT
								return repository.save(account);
								
							});
						
					});
				});
	}

	@Override
	public Mono<Boolean> checkIfHaveMoreThanOneAccount(String personCode) {
		return repository.countByPersonCode(personCode).flatMap(x -> {
			if(x > 0) {
				return Mono.just(true);
			}
			return Mono.just(false);
		});
	}

	@Override
	public Mono<Double> getMinimumOpeningAmount(String accountType) {
		
		switch(PassiveAccountTypeEnum.valueOf(accountType)){
		case CURRENT_ACCOUNT :
			return Mono.just(1.0);
		case FIX_TERM_ACCOUNT :
			return Mono.just(2.0);
		case SAVING_ACCOUNT :
			return Mono.just(3.0);
		case VIP :
			return Mono.just(4.0);
		case PYME :
			return Mono.just(5.0);	
		default:
			return Mono.just(0.0);
		} 
	}

	@Override
	public Mono<Integer> getMaximumTransactions(String accountNumber) {
		return repository.findByAccountNumber(accountNumber).next().flatMap(x -> {
			return x.getMaximumTransactions()!=null?Mono.just(x.getMaximumTransactions()):Mono.just(0);
		});
	}

	@Override
	public Mono<Integer> getMaximumTransactionsWithoutCommission(String accountNumber) {
		return repository.findByAccountNumber(accountNumber).next().flatMap(x -> {
			return x.getMaximumTransactionsWithoutCommission()!=null?Mono.just(x.getMaximumTransactionsWithoutCommission()):Mono.just(0);
		});
	}
	
	@Override
	public Mono<Double> getTransactionCommission(String accountNumber) {
		return repository.findByAccountNumber(accountNumber).next().flatMap(x -> {
			return x.getTransactionCommission()!=null?Mono.just(x.getTransactionCommission()):Mono.just(0.0);
		});
	}
	
	@Override
	public Mono<Double> getMaintenanceCommission(String accountNumber) {
		return repository.findByAccountNumber(accountNumber).next().flatMap(x -> {
			return x.getMaintenanceCommission()!=null?Mono.just(x.getMaintenanceCommission()):Mono.just(0.0);
		});
	}

	@Override
	public Mono<Integer> getDayMovementAvailable(String accountNumber) {
		return repository.findByAccountNumber(accountNumber).next().flatMap(x -> {
			return x.getDayMovementAvailable()!=null?Mono.just(x.getDayMovementAvailable()):Mono.just(0);
		});
	}
	

	


	@Override
	public Mono<Boolean> checkIfExist(String accountNumber) {
		return repository.findByAccountNumber(accountNumber)
				.switchIfEmpty(x -> Mono.just(false))
				.count().flatMap(x -> {
			if(x > 0) {
				return Mono.just(true);
			}
			return Mono.just(false);
		});
	}

	@Override
	public Mono<PassiveAccountCollection> updateAccountBalance(String accountNumber, Double newAccountBalance) {
		return repository.findByAccountNumber(accountNumber).next().flatMap(x -> {
			x.setAccountBalance(newAccountBalance);
			return repository.save(x);
		});
	}

	@Override
	public Mono<Double> getAccountBalance(String accountNumber) {
		return repository.findByAccountNumber(accountNumber).next()
				.map(x -> x.getAccountBalance()!=null?x.getAccountBalance():0.0);
	}

	@Override
	public Mono<PassiveAccountCollection> updateDebitCardNumber(String accountNumber, String debitCardNumber) {
		return repository.findByAccountNumber(accountNumber).next()
				.flatMap(account -> {
			account.setDebitCardNumber(debitCardNumber);
			return repository.save(account);
		});
	}

	

	

	@Override
	public Mono<String> getAccountNumberAvailable(String personCode, Double movementAmount) {
		return repository.findByPersonCode(personCode)
				.filter(account -> account.getAccountBalance().compareTo(movementAmount) > 0)
				.next()
				.map(x -> x.getAccountNumber());
	}
	
	
	@Override
	public Flux<PassiveAccountCollection> getPassiveAccountsWithChargeCommissionPending(Date chargeCommissionDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<PassiveAccountCollection> updateChargeCommissionDate(String accountNumber, Date chargeCommissionDate) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
