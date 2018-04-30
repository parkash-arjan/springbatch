package com.fibonacci.springbatch.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CustomerRowMapper implements RowMapper<Customer> {
	@Override
	public Customer mapRow(ResultSet resultSet, int i) throws SQLException {

		//@formatter:off
		return new Customer(
												resultSet.getLong("id"), 
												resultSet.getString("firstName"), 
												resultSet.getString("lastName") 
												);
		//@formatter:on		
	}
}
