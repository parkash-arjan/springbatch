package com.fibonacci.springbatch.xml_files;

import java.util.Date;

public class Customer {

	private final long id;

	private final String firstName;

	private final String lastName;

	private final Date birthdate;

	public Customer(long id, String firstName, String lastName, Date birthdate) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthdate = birthdate;
	}

	@Override
	public String toString() {
		return String.format("Customer [id=%s, firstName=%s, lastName=%s, birthdate=%s]", id, firstName, lastName, birthdate);
	}

}
