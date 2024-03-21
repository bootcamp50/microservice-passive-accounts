package com.nttdata.microservice.bankpassiveaccounts.dto;

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
public class CreditInfoDto {
	private Double interestPercentage;
    private Integer closingDay;

}
