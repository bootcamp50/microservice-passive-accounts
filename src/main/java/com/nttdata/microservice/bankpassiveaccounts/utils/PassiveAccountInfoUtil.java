package com.nttdata.microservice.bankpassiveaccounts.utils;

public interface PassiveAccountInfoUtil {
	
	Double roundDouble(Double numberToRound, int decimalPlaces);
    Double applyInterests(Double amount, Double interestPercentage);
    Double calculateCommission(Double amount, Double interestPercentage);

}
