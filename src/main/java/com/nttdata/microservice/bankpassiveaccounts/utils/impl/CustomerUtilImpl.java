package com.nttdata.microservice.bankpassiveaccounts.utils.impl;

import org.springframework.stereotype.Component;

import com.nttdata.microservice.bankpassiveaccounts.collections.Customer;
import com.nttdata.microservice.bankpassiveaccounts.dto.CustomerDto;
import com.nttdata.microservice.bankpassiveaccounts.utils.CustomerUtil;

@Component
public class CustomerUtilImpl implements CustomerUtil {

	@Override
	public Customer toCustomer(CustomerDto customerDto) {
		return Customer.builder()
                .id(customerDto.getId())
                .customerType(customerDto.getPersonType())
                .state(customerDto.getState())
                .build();
	}

}
