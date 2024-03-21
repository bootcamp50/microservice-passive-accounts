package com.nttdata.microservice.bankpassiveaccounts.dto;

import java.util.Date;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreditDto {
	private ObjectId id;
	private CustomerDto customer;
	private String creditType;
	private String creditNumber;
	private CreditInfoDto creditInfo;
	private Date paymentDate;
	private Date issueDate;

	private String state;

}
