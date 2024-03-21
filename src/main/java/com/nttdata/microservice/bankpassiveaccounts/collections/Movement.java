package com.nttdata.microservice.bankpassiveaccounts.collections;

import java.util.Date;
import java.util.UUID;

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
public class Movement {
	private String operationNumber = UUID.randomUUID().toString();
    private Date time;
    private String type;
    private Double amount;
    private Double commission;
    private Double finalBalance;
}
