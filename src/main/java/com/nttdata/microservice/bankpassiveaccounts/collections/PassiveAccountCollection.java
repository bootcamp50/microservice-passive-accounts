package com.nttdata.microservice.bankpassiveaccounts.collections;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "passive_accounts")
public class PassiveAccountCollection {
	
	@Id
	private ObjectId id;
	
	private String accountType;
	private String personType;
	private String accountNumber;
	private String personCode;
	
	private String debitCardNumber;
	private String walletNumber;
	
	private Double accountAmount;
	private Double accountBalance;
	private Double maintenanceCommission;
	private Double transactionCommission;
	private Date chargeCommisionDate;
	
	private Double minimumOpenningAmount;
	private Double minimumAverageAmount;
	private Integer maximumTransactionsWithoutCommission;
	private Integer maximumTransactions;
	
	private Integer dayMovementAvailable;
	
	private String state;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;
	
}
