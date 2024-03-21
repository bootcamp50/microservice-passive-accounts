package com.nttdata.microservice.bankpassiveaccounts.dto;

import java.util.ArrayList;
import java.util.Date;

import com.nttdata.microservice.bankpassiveaccounts.collections.AccountInfo;
import com.nttdata.microservice.bankpassiveaccounts.collections.Movement;
import com.nttdata.microservice.bankpassiveaccounts.collections.Person;

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
public class PassiveAccountDto {
	private String id;
	private String customerId;
    private String accountType;
    private String accountNumber;
    private String state;
    private AccountInfo accountInfo;
    private Date issueDate;
    private Date dueDate;
    private Double currentBalance;
    private Integer doneOperationsInMonth;
    private ArrayList<Movement> movements;
    private ArrayList<Person> holders;
    private ArrayList<Person> signers;
}
