package com.nttdata.microservice.bankpassiveaccounts.utils;

import com.nttdata.microservice.bankpassiveaccounts.collections.Customer;
import com.nttdata.microservice.bankpassiveaccounts.dto.CustomerDto;


public interface CustomerUtil {
	Customer toCustomer(CustomerDto customerDto);
}
