package com.nttdata.microservice.bankpassiveaccounts.collections;

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
public class AccountInfo {
	private Double maintenanceCommissionPercentage;
    private Double minimumDailyAverage;
    private Double operationCommissionPercentage;
    private Integer maximumNumberOfOperations;
    private Integer allowedDayForOperations;

}
