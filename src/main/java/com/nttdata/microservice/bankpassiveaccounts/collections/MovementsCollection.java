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
@Document(value = "movements")
public class MovementsCollection {
	
	@Id
	private ObjectId id;
	
	private String personCode;
	private String walletNumber;
	private String debitCardNumber;
	private String accountNumberSource;
	private String accountNumberDestination;
	
	private Double amount;
	private String movementType;
	
	private Double transactionCommission;
	
	private String state;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;

}
