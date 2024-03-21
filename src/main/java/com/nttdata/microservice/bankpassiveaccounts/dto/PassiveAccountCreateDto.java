package com.nttdata.microservice.bankpassiveaccounts.dto;

import java.util.ArrayList;
import java.util.Date;

import com.nttdata.microservice.bankpassiveaccounts.collections.AccountInfo;
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
public class PassiveAccountCreateDto {
	private String customerId;
    private String accountType;
    private AccountInfo accountInfo;
    private Date issueDate;
    private Date dueDate;
    private ArrayList<Person> holders;
    private ArrayList<Person> signers;

}
