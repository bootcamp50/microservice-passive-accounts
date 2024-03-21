package com.nttdata.microservice.bankpassiveaccounts.collections;

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
public class Person {
	private String name;
    private String lastname;
    private String identityNumber;
    private String address;
    private String email;
    private String phoneNumber;
    private String mobileNumber;
    private Date birthdate;
}
