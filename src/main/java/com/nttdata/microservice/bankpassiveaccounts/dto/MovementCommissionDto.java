package com.nttdata.microservice.bankpassiveaccounts.dto;

import java.util.Date;

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
public class MovementCommissionDto {
	private String operationNumber;
    private Date time;
    private Double amount;
    private Double commission;
}
