package com.nttdata.microservice.bankpassiveaccounts.services.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.microservice.bankpassiveaccounts.collections.MovementsCollection;
import com.nttdata.microservice.bankpassiveaccounts.collections.enums.MovementTypeEnum;
import com.nttdata.microservice.bankpassiveaccounts.repository.IMovementRepository;
import com.nttdata.microservice.bankpassiveaccounts.services.IDebitCardService;
import com.nttdata.microservice.bankpassiveaccounts.services.IMovementService;
import com.nttdata.microservice.bankpassiveaccounts.services.IPassiveAccountService;
import com.nttdata.microservice.bankpassiveaccounts.services.IWalletService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovementServiceImpl implements IMovementService{

	@Autowired
	private IMovementRepository repository;
	
	@Autowired
	private IPassiveAccountService passiveAccountService;
	
	@Autowired
	private IDebitCardService debitCardService;
	
	@Autowired
	private IWalletService walletService;

	@Override
	public Mono<Void> saveDeposit(MovementsCollection collection) {
		
		// SET DEFAULT VALUES
		collection.setMovementType(MovementTypeEnum.DEPOSIT.toString());
		
		// CHECK MAXIMUM TRANSACTIONS
		 return checkMaximumTransactions(collection.getAccountNumberDestination())
				 .flatMap(checkMaximumTransactions -> {
					 if(checkMaximumTransactions) {
						 
						 // CHECK DAY MOVEMENT AVAILABLE
						 return checkDayMovementAvailable(collection.getAccountNumberDestination())
								 .flatMap(checkDayMovementAvailable -> {
									 if(checkDayMovementAvailable) {
										 
										 // GET AMOUNT BALANCE
										 return passiveAccountService.getAccountBalance(collection.getAccountNumberDestination())
												 .flatMap(amountBalance -> {
													 
													 // CHECK ACCOUNT BALANCE 
													 //if(amountBalance > collection.getAmount()) {
														 
														// GET TRANSACTION COMMISSION
														 return passiveAccountService.getTransactionCommission(collection.getAccountNumberDestination())
															 	.flatMap(transactionCommission -> {
															 		
															 	    // CHECK MAXIMUM TRANSACTIONS WITHOUT COMMISSION
															 		return checkMaximumTransactionsWithoutCommission(collection.getAccountNumberDestination())
															 				.flatMap(checkMaximumTransactionsWithoutCommission -> {
															 					if(checkMaximumTransactionsWithoutCommission) {
															 						
															 						// ADD TRANSACTION COMMISSSION
															 						collection.setTransactionCommission(transactionCommission);
																				}
															 					
															 					
															 					// SAVE MOVEMENT
																				repository.save(collection).subscribe();
																				
																				// UPDATE NEW BALANCE
															 					Double newAmountBalance = amountBalance + collection.getAmount();
															 					passiveAccountService.updateAccountBalance(collection.getAccountNumberDestination(), newAmountBalance).subscribe();
																				
																				return Mono.empty();
																				
																				
																			 });
															 	});
														 
													 //}
													 
													 //return Mono.error(RuntimeException::new);
											 
										 });  
									 }
									 return Mono.error(RuntimeException::new);
						 });
					 }
					 return Mono.error(RuntimeException::new);
				 });
	}

	@Override
	public Mono<Void> saveWithdrawal(MovementsCollection collection) {

		collection.setMovementType(MovementTypeEnum.WITHDRAWAL.toString());
		
		// CHECK MAXIMUM TRANSACTIONS
		return checkMaximumTransactions(collection.getAccountNumberDestination())
				 .flatMap(checkMaximumTransactions -> {
					 if(checkMaximumTransactions) {
						 
						 // CHECK DAY MOVEMENT AVAILABLE
				 return checkDayMovementAvailable(collection.getAccountNumberDestination())
						 .flatMap(checkDayMovementAvailable -> {
							 if(checkDayMovementAvailable) {
								 
								 // GET AMOUNT BALANCE
								 return passiveAccountService.getAccountBalance(collection.getAccountNumberDestination())
										 .flatMap(amountBalance -> {
											 
											// CHECK ACCOUNT BALANCE 
											 if(amountBalance > collection.getAmount()) {
											 	 
											 // GET TRANSACTION COMMISSION
											 return passiveAccountService.getTransactionCommission(collection.getAccountNumberDestination())
												 	.flatMap(transactionCommission -> {
												 		
												 	    // CHECK MAXIMUM TRANSACTIONS WITHOUT COMMISSION
												 		return checkMaximumTransactionsWithoutCommission(collection.getAccountNumberDestination())
												 				.flatMap(checkMaximumTransactionsWithoutCommission -> {
												 					if(checkMaximumTransactionsWithoutCommission) {
												 						
												 						// ADD TRANSACTION COMMISSSION
												 						collection.setTransactionCommission(transactionCommission);
																	}
												 					
												 					// SAVE MOVEMENT
																	repository.save(collection).subscribe();
																	
																	// UPDATE NEW BALANCE
												 					Double newAmountBalance = amountBalance - collection.getAmount();
												 					passiveAccountService.updateAccountBalance(collection.getAccountNumberDestination(), newAmountBalance).subscribe();
												 					
																	return Mono.empty();
																	
																 });
												 	});
											 }
											 return Mono.error(RuntimeException::new);
								 });  
							 }
							 return Mono.error(RuntimeException::new);
				 });
			 }
			 return Mono.error(RuntimeException::new);
		 });
				 
	}

	@Override
	public Mono<MovementsCollection> saveTransferWithSameAccount(MovementsCollection collection) {

		collection.setMovementType(MovementTypeEnum.TRANSFER_SAME.toString());
		
		
		// CHECK MAXIMUM TRANSACTIONS
		return checkMaximumTransactions(collection.getAccountNumberSource())
				 .flatMap(checkMaximumTransactions -> {
					 if(checkMaximumTransactions) {
						 
						 // CHECK DAY MOVEMENT AVAILABLE
				 return checkDayMovementAvailable(collection.getAccountNumberSource())
						 .flatMap(checkDayMovementAvailable -> {
							 if(checkDayMovementAvailable) {
								 
								 // GET SOURCE AMOUNT BALANCE
								 return passiveAccountService.getAccountBalance(collection.getAccountNumberSource())
										 .flatMap(sourceAmountBalance -> {
											 
											// GET DESTINATION AMOUNT BALANCE
											 return passiveAccountService.getAccountBalance(collection.getAccountNumberSource())
													 .flatMap(destinationAmountBalance -> {
														 
													 
												// CHECK ACCOUNT BALANCE 
												 if(sourceAmountBalance > collection.getAmount()) {
											 	 
												 // GET TRANSACTION COMMISSION
												 return passiveAccountService.getTransactionCommission(collection.getAccountNumberSource())
													 	.flatMap(transactionCommission -> {
													 		
													 	    // CHECK MAXIMUM TRANSACTIONS WITHOUT COMMISSION
													 		return checkMaximumTransactionsWithoutCommission(collection.getAccountNumberSource())
													 				.flatMap(checkMaximumTransactionsWithoutCommission -> {
													 					if(checkMaximumTransactionsWithoutCommission) {
													 						
													 						// ADD TRANSACTION COMMISSSION
													 						collection.setTransactionCommission(transactionCommission);
																		}
													 					
													 					// SAVE MOVEMENT
																		repository.save(collection).subscribe();
																		
																		// UPDATE NEW SOURCE BALANCE
													 					Double newSourceAmountBalance = sourceAmountBalance - collection.getAmount();
													 					passiveAccountService.updateAccountBalance(collection.getAccountNumberSource(), newSourceAmountBalance).subscribe();
													 					
													 				    // UPDATE NEW DESTINATION BALANCE
													 					Double newDestinationAmountBalance = destinationAmountBalance + collection.getAmount();
													 					passiveAccountService.updateAccountBalance(collection.getAccountNumberDestination(), newDestinationAmountBalance).subscribe();
													 					
																		return Mono.empty();
																		
																	 });
													 	});
												 
													 }
													 return Mono.error(RuntimeException::new);
													 });
											 
								 });  
							 }
							 return Mono.error(RuntimeException::new);
				 });
			 }
			 return Mono.error(RuntimeException::new);
		 });
	}

	@Override
	public Mono<MovementsCollection> saveTransferThirdAccount(MovementsCollection collection) {


		collection.setMovementType(MovementTypeEnum.TRANSFER_THIRD.toString());
		
		
		// CHECK MAXIMUM TRANSACTIONS
		return checkMaximumTransactions(collection.getAccountNumberSource())
				 .flatMap(checkMaximumTransactions -> {
					 if(checkMaximumTransactions) {
						 
						 // CHECK DAY MOVEMENT AVAILABLE
				 return checkDayMovementAvailable(collection.getAccountNumberSource())
						 .flatMap(checkDayMovementAvailable -> {
							 if(checkDayMovementAvailable) {
								 
								 // GET SOURCE AMOUNT BALANCE
								 return passiveAccountService.getAccountBalance(collection.getAccountNumberSource())
										 .flatMap(sourceAmountBalance -> {
											 
											// GET DESTINATION AMOUNT BALANCE
											 return passiveAccountService.getAccountBalance(collection.getAccountNumberSource())
													 .flatMap(destinationAmountBalance -> {
											 
												// CHECK ACCOUNT BALANCE 
												 if(sourceAmountBalance > collection.getAmount()) {
													 	
												 // GET TRANSACTION COMMISSION
												 return passiveAccountService.getTransactionCommission(collection.getAccountNumberSource())
													 	.flatMap(transactionCommission -> {
													 		
													 	    // CHECK MAXIMUM TRANSACTIONS WITHOUT COMMISSION
													 		return checkMaximumTransactionsWithoutCommission(collection.getAccountNumberSource())
													 				.flatMap(checkMaximumTransactionsWithoutCommission -> {
													 					if(checkMaximumTransactionsWithoutCommission) {
													 						
													 						// ADD TRANSACTION COMMISSSION
													 						collection.setTransactionCommission(transactionCommission);
																		}
													 					
													 					// SAVE MOVEMENT
																		repository.save(collection).subscribe();
																		
																		// UPDATE NEW SOURCE BALANCE
													 					Double newSourceAmountBalance = sourceAmountBalance - collection.getAmount();
													 					passiveAccountService.updateAccountBalance(collection.getAccountNumberSource(), newSourceAmountBalance).subscribe();
													 					
													 				    // UPDATE NEW DESTINATION BALANCE
													 					Double newDestinationAmountBalance = destinationAmountBalance + collection.getAmount();
													 					passiveAccountService.updateAccountBalance(collection.getAccountNumberDestination(), newDestinationAmountBalance).subscribe();
													 					
																		return Mono.empty();
																		
																	 });
													 	});
													 }
													 return Mono.error(RuntimeException::new);
												 
													 });
								 });  
							 }
							 return Mono.error(RuntimeException::new);
				 });
			 }
			 return Mono.error(RuntimeException::new);
		 });
	}


	@Override
	public Mono<MovementsCollection> saveWithdrawalWithDebitCard(MovementsCollection collection) {
		
		collection.setMovementType(MovementTypeEnum.WITHDRAWAL_DEBIT_CARD.toString());

		// GET ACCOUNT
		Mono<String> accountNumberMono = debitCardService.getMainAccountNumber(collection.getDebitCardNumber())
				.flatMap( mainAccountNumber -> {
					// GET MAIN AMOUNT BALANCE
					 return passiveAccountService.getAccountBalance(mainAccountNumber)
							 .flatMap(mainAmountBalance -> {
								//CHECK MAIN AMOUNT BALANCE
								 if(Double.compare(mainAmountBalance,collection.getAmount()) < 0) {
									// GET AVAILABLE ACCOUNT
									 return passiveAccountService.getAccountNumberAvailable(collection.getPersonCode(), collection.getAmount());
								 }
								 return Mono.just(mainAccountNumber);
							 });
				});
		accountNumberMono.subscribe();
		
		
		// PROCCESS ACCOUNT
		return accountNumberMono.flatMap(accountNumber -> {
			
			collection.setAccountNumberSource(accountNumber);
			
			// CHECK MAXIMUM TRANSACTIONS
			return checkMaximumTransactions(collection.getAccountNumberSource())
					 .flatMap(checkMaximumTransactions -> {
						 if(checkMaximumTransactions) {
							 
							 // CHECK DAY MOVEMENT AVAILABLE
					 return checkDayMovementAvailable(collection.getAccountNumberSource())
							 .flatMap(checkDayMovementAvailable -> {
								 if(checkDayMovementAvailable) {
									 
									 // GET SOURCE AMOUNT BALANCE
									 return passiveAccountService.getAccountBalance(collection.getAccountNumberSource())
											 .flatMap(sourceAmountBalance -> {
												 
												// GET DESTINATION AMOUNT BALANCE
												 return passiveAccountService.getAccountBalance(collection.getAccountNumberSource())
														 .flatMap(destinationAmountBalance -> {
												 
												 	 
													 // GET TRANSACTION COMMISSION
													 return passiveAccountService.getTransactionCommission(collection.getAccountNumberSource())
														 	.flatMap(transactionCommission -> {
														 		
														 	    // CHECK MAXIMUM TRANSACTIONS WITHOUT COMMISSION
														 		return checkMaximumTransactionsWithoutCommission(collection.getAccountNumberSource())
														 				.flatMap(checkMaximumTransactionsWithoutCommission -> {
														 					if(checkMaximumTransactionsWithoutCommission) {
														 						
														 						// ADD TRANSACTION COMMISSSION
														 						collection.setTransactionCommission(transactionCommission);
																			}
														 					
														 					// SAVE MOVEMENT
																			repository.save(collection).subscribe();
																			
																			// UPDATE NEW SOURCE BALANCE
														 					Double newSourceAmountBalance = sourceAmountBalance - collection.getAmount();
														 					passiveAccountService.updateAccountBalance(collection.getAccountNumberSource(), newSourceAmountBalance).subscribe();
														 					
														 				    // UPDATE NEW DESTINATION BALANCE
														 					Double newDestinationAmountBalance = destinationAmountBalance + collection.getAmount();
														 					passiveAccountService.updateAccountBalance(collection.getAccountNumberDestination(), newDestinationAmountBalance).subscribe();
														 					
																			return Mono.empty();
																			
																		 });
														 	});
														 });
									 });  
								 }
								 return Mono.error(RuntimeException::new);
					 });
				 }
				 return Mono.error(RuntimeException::new);
			 });
			
		});
	}

	@Override
	public Flux<MovementsCollection> getByAccountNumber(String accountNumber) {
		return repository
				.findAll()
				.filter(x -> accountNumber.equals(x.getAccountNumberSource()) || accountNumber.equals(x.getAccountNumberDestination()));
	}

	@Override
	public Mono<Boolean> checkMaximumTransactions(String accountNumber) {
		
		// GET MAXIMUM TRANSACTIONS
		return passiveAccountService.getMaximumTransactions(accountNumber)
				.switchIfEmpty(Mono.just(0))
				.flatMap(maximumTransactions -> {
					//GET TRANSACTIONS COUNT
					return repository
							.findAll()
							.filter(x -> accountNumber.equals(x.getAccountNumberSource()) || accountNumber.equals(x.getAccountNumberDestination()))
							.switchIfEmpty(Flux.empty())
							.count()
							.flatMap(totalTransactions -> {
								
								// VERIFY MAXIMUM TRANSACTIONS
								if(Integer.compare(0, maximumTransactions)  < 0 
										&& Integer.compare(totalTransactions.intValue(), maximumTransactions)  < 0 ) {
									return Mono.just(false);	
								}
								return Mono.just(true);
					});
				});
	}

	@Override
	public Mono<Boolean> checkMaximumTransactionsWithoutCommission(String accountNumber) {
		
		// GET MAXIMUM TRANSACTIONS
		return passiveAccountService.getMaximumTransactionsWithoutCommission(accountNumber)
				.flatMap(maximumTransactions -> {
					
					//GET TRANSACTIONS COUNT
					return repository
							.findAll()
							.filter(x -> accountNumber.equals(x.getAccountNumberSource()) || accountNumber.equals(x.getAccountNumberDestination()))
							.count()
							.flatMap(totalTransactions -> {
								
								// VERIFY MAXIMUM TRANSACTIONS
								if(totalTransactions < maximumTransactions) {
									return Mono.just(false);	
								}
								return Mono.just(true);
					});
				});
	}

	@Override
	public Mono<Boolean> checkDayMovementAvailable(String accountNumber) {
		
		// GET DAY MOVEMENT AVAILABLE
		return passiveAccountService.getDayMovementAvailable(accountNumber)
				.flatMap(dayMovementAvailable -> {
					
					LocalDate localDate = LocalDate.now();
					
					// CHECK DAY MOVEMENT AVAILABLE
					if(dayMovementAvailable == null || dayMovementAvailable == 0) {
						return Mono.just(true);
					}else if(dayMovementAvailable == localDate.getDayOfMonth()) {
						return Mono.just(true);
					}else {
						return Mono.just(false);	
					}
					
				});
	}

	@Override
	public Mono<Boolean> checkIfHaveAverageAmount(String accountNumber, Double minimumAverageAmount) {
		
		// SET START DATA AND END DATE
		LocalDate localDateLastMonth = LocalDate.now().minusMonths(1);
		YearMonth lastMonth = YearMonth.from(localDateLastMonth);
		LocalDate start = lastMonth.atDay(1);
		LocalDate end   = lastMonth.atEndOfMonth();
		
	    
	    
	    //Date start = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	    //localDate = localDate.minusMonths(1);
	    //Date end = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

	    
					//GET MOVEMENTS SUM AMOUNT A MONTH AGO
					return getByAccountNumber(accountNumber)
							.filter( mo ->  
								 mo.getCreatedAt().toInstant()
								.atZone(ZoneId.systemDefault())
								.toLocalDate().isAfter(start)
								
								&& mo.getCreatedAt().toInstant()
								.atZone(ZoneId.systemDefault())
								.toLocalDate().isBefore(end)
							)
							.map(movement -> movement.getAmount())
							.reduce(0.0, (x1, x2) -> x1 + x2)
							.flatMap(suma -> {
								
								//GET MOVEMENTS COUNT A MONTH AGO
								return getByAccountNumber(accountNumber)
										.filter( m -> m.getCreatedAt().toInstant()
												.atZone(ZoneId.systemDefault())
												.toLocalDate().isAfter(start)
												
												&& m.getCreatedAt().toInstant()
												.atZone(ZoneId.systemDefault())
												.toLocalDate().isBefore(end)
												)
										.count()
										.flatMap(cantidad -> {
											
											// CHECK IF MINIMUM AVERAGE AMOUNT IS GREATHER THAM CALCULATED AVERAGE AMOUNT 
											if( (suma/cantidad) >= minimumAverageAmount) {
												return Mono.just(true);	
											}
											return Mono.just(false);
										});
								
							});
							
				
	}

	@Override
	public Mono<MovementsCollection> saveWithdrawalWithWallet(MovementsCollection collection) {
		collection.setMovementType(MovementTypeEnum.WITHDRAWAL_WALLET.toString());
		
		//GET DEBIT CARD
		
		Mono<String> debitCardNumberMono =  walletService.getDebitCardNumber(collection.getWalletNumber());
		debitCardNumberMono.subscribe();
		
		
		return debitCardNumberMono.flatMap( debitCardNumber -> {
			
			return debitCardService.getMainAccountNumber(debitCardNumber).flatMap(mainAccountNumber -> {
				
				// GET MAIN AMOUNT BALANCE
				 return passiveAccountService.getAccountBalance(mainAccountNumber)
						 .flatMap(mainAmountBalance -> {
							//CHECK MAIN AMOUNT BALANCE
							 if(Double.compare(mainAmountBalance,collection.getAmount()) < 0) {
								// GET AVAILABLE ACCOUNT
								 return Mono.error(RuntimeException::new);
							 }
							 
							 //return Mono.just(mainAccountNumber);
							 
							 collection.setAccountNumberSource(mainAccountNumber);
								
								// CHECK MAXIMUM TRANSACTIONS
								return checkMaximumTransactions(collection.getAccountNumberSource())
										 .flatMap(checkMaximumTransactions -> {
											 if(checkMaximumTransactions) {
												 
												 // CHECK DAY MOVEMENT AVAILABLE
										 return checkDayMovementAvailable(collection.getAccountNumberSource())
												 .flatMap(checkDayMovementAvailable -> {
													 if(checkDayMovementAvailable) {
														 
														 // GET SOURCE AMOUNT BALANCE
														 return passiveAccountService.getAccountBalance(collection.getAccountNumberSource())
																 .flatMap(sourceAmountBalance -> {
																	 
																	// GET DESTINATION AMOUNT BALANCE
																	 return passiveAccountService.getAccountBalance(collection.getAccountNumberSource())
																			 .flatMap(destinationAmountBalance -> {
																	 
																	 	 
																		 // GET TRANSACTION COMMISSION
																		 return passiveAccountService.getTransactionCommission(collection.getAccountNumberSource())
																			 	.flatMap(transactionCommission -> {
																			 		
																			 	    // CHECK MAXIMUM TRANSACTIONS WITHOUT COMMISSION
																			 		return checkMaximumTransactionsWithoutCommission(collection.getAccountNumberSource())
																			 				.flatMap(checkMaximumTransactionsWithoutCommission -> {
																			 					if(checkMaximumTransactionsWithoutCommission) {
																			 						
																			 						// ADD TRANSACTION COMMISSSION
																			 						collection.setTransactionCommission(transactionCommission);
																								}
																			 					
																			 					// SAVE MOVEMENT
																								repository.save(collection).subscribe();
																								
																								// UPDATE NEW SOURCE BALANCE
																			 					Double newSourceAmountBalance = sourceAmountBalance - collection.getAmount();
																			 					passiveAccountService.updateAccountBalance(collection.getAccountNumberSource(), newSourceAmountBalance).subscribe();
																			 					
																			 				    // UPDATE NEW DESTINATION BALANCE
																			 					Double newDestinationAmountBalance = destinationAmountBalance + collection.getAmount();
																			 					passiveAccountService.updateAccountBalance(collection.getAccountNumberDestination(), newDestinationAmountBalance).subscribe();
																			 					
																								return Mono.empty();
																								
																							 });
																			 	});
																			 });
														 });  
													 }
													 return Mono.error(RuntimeException::new);
										 });
									 }
									 return Mono.error(RuntimeException::new);
								 });
							 
						 });
				
			});
		});
		

		// GET ACCOUNT
		/*Mono<String> accountNumberMono = debitCardService.getMainAccountNumber(collection.getDebitCardNumber())
				.flatMap( mainAccountNumber -> {
					// GET MAIN AMOUNT BALANCE
					 return passiveAccountService.getAccountBalance(mainAccountNumber)
							 .flatMap(mainAmountBalance -> {
								//CHECK MAIN AMOUNT BALANCE
								 if(Double.compare(mainAmountBalance,collection.getAmount()) < 0) {
									// GET AVAILABLE ACCOUNT
									 return passiveAccountService.getAccountNumberAvailable(collection.getPersonCode(), collection.getAmount());
								 }
								 return Mono.just(mainAccountNumber);
							 });
				});
		accountNumberMono.subscribe();
		
		
		// PROCCESS ACCOUNT
		return accountNumberMono.flatMap(accountNumber -> {
			
			collection.setAccountNumberSource(accountNumber);
			
			// CHECK MAXIMUM TRANSACTIONS
			return checkMaximumTransactions(collection.getAccountNumberSource())
					 .flatMap(checkMaximumTransactions -> {
						 if(checkMaximumTransactions) {
							 
							 // CHECK DAY MOVEMENT AVAILABLE
					 return checkDayMovementAvailable(collection.getAccountNumberSource())
							 .flatMap(checkDayMovementAvailable -> {
								 if(checkDayMovementAvailable) {
									 
									 // GET SOURCE AMOUNT BALANCE
									 return passiveAccountService.getAccountBalance(collection.getAccountNumberSource())
											 .flatMap(sourceAmountBalance -> {
												 
												// GET DESTINATION AMOUNT BALANCE
												 return passiveAccountService.getAccountBalance(collection.getAccountNumberSource())
														 .flatMap(destinationAmountBalance -> {
												 
												 	 
													 // GET TRANSACTION COMMISSION
													 return passiveAccountService.getTransactionCommission(collection.getAccountNumberSource())
														 	.flatMap(transactionCommission -> {
														 		
														 	    // CHECK MAXIMUM TRANSACTIONS WITHOUT COMMISSION
														 		return checkMaximumTransactionsWithoutCommission(collection.getAccountNumberSource())
														 				.flatMap(checkMaximumTransactionsWithoutCommission -> {
														 					if(checkMaximumTransactionsWithoutCommission) {
														 						
														 						// ADD TRANSACTION COMMISSSION
														 						collection.setTransactionCommission(transactionCommission);
																			}
														 					
														 					// SAVE MOVEMENT
																			repository.save(collection).subscribe();
																			
																			// UPDATE NEW SOURCE BALANCE
														 					Double newSourceAmountBalance = sourceAmountBalance - collection.getAmount();
														 					passiveAccountService.updateAccountBalance(collection.getAccountNumberSource(), newSourceAmountBalance).subscribe();
														 					
														 				    // UPDATE NEW DESTINATION BALANCE
														 					Double newDestinationAmountBalance = destinationAmountBalance + collection.getAmount();
														 					passiveAccountService.updateAccountBalance(collection.getAccountNumberDestination(), newDestinationAmountBalance).subscribe();
														 					
																			return Mono.empty();
																			
																		 });
														 	});
														 });
									 });  
								 }
								 return Mono.error(RuntimeException::new);
					 });
				 }
				 return Mono.error(RuntimeException::new);
			 });
			
		});*/
	}

	
	
	
}
