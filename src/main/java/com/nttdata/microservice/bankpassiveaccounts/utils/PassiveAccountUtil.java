package com.nttdata.microservice.bankpassiveaccounts.utils;


import com.nttdata.microservice.bankpassiveaccounts.collections.PassiveAccountCollection;
import com.nttdata.microservice.bankpassiveaccounts.dto.PassiveAccountBalanceDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.PassiveAccountCreateDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.PassiveAccountDto;

public interface PassiveAccountUtil {
	
	PassiveAccountCollection toPassiveAccountCollection(PassiveAccountCreateDto accountDto);
	PassiveAccountDto toPassiveAccountDto(PassiveAccountCollection account);
	
	PassiveAccountBalanceDto toPassiveAccountBalanceDto(PassiveAccountCollection account);

}
