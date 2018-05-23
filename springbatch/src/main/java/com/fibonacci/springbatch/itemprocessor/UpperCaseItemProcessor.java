package com.fibonacci.springbatch.itemprocessor;

import java.util.Date;

import org.springframework.batch.item.ItemProcessor;

public class UpperCaseItemProcessor implements ItemProcessor<Customer, Customer> {

	@Override
	public Customer process(Customer item) throws Exception {
		//return new Customer(item.getId(), item.getFirstName().toUpperCase(), item.getLastName().toUpperCase(), item.getBirthdate());
		return new Customer(item.getId(), item.getFirstName().toUpperCase(), item.getLastName().toUpperCase(), new Date());
	}
}
