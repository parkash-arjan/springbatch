package com.fibonacci.springbatch.database;

import java.util.Date;

public class Customer {

	private final long id;

	private final String firstName;

	private final String lastName;

	private final Date birthdate;

	public Customer(long id, String firstName, String lastName) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthdate = new Date();
	}

	public long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	@Override
	public String toString() {
		return String.format("Customer [id=%s, firstName=%s, lastName=%s, birthdate=%s]", id, firstName, lastName, birthdate);
	}

}
