package com.nttdata.microservice.bankpassiveaccounts.exceptions;

public class PassiveAccountException extends RuntimeException  {
	private static final long serialVersionUID = -5713584292717311039L;

    public PassiveAccountException(String s) {
        super(s);
    }
}
