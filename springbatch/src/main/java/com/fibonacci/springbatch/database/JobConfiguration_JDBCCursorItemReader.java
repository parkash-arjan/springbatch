package com.fibonacci.springbatch.database;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class JobConfiguration_JDBCCursorItemReader {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource dataSource;

	@Bean
	public JdbcCursorItemReader<Customer> jdbcCursorItemReader() {

		// ResultSet and JdbcCursorItemReader are not thread safe..
		// ORDER BY is necessary because in case of failure wont be able to know
		// where to start
		JdbcCursorItemReader<Customer> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		// String sql = "SELECT ID , FIRSTNAME , LASTNAME , BIRTHDATE FROM
		// CUSTOMER ORDER BY LASTNAME , FIRSTNAME";
		String sql = "SELECT ID , FIRSTNAME , LASTNAME , BIRTHDATE FROM CUSTOMER ORDER BY ID";
		jdbcCursorItemReader.setSql(sql);
		jdbcCursorItemReader.setDataSource(dataSource);
		jdbcCursorItemReader.setRowMapper(new CustomerRowMapper());

		return jdbcCursorItemReader;

	}

	@Bean
	public ItemWriter<Customer> itemWriter() {
		System.out.println("*****************************************************");
		return items -> {
			System.out.println("items.size()  " + items.size());
			for (Customer customer : items) {
				System.out.println(customer);
			}
		};
	}

	@Bean
	public Step step() {
		//@formatter:off
		TaskletStep taskletStep= stepBuilderFactory
													.get("step1234")
													.<Customer, Customer>chunk(10)
													.reader(jdbcCursorItemReader())
													.writer(itemWriter())
													.build();
		//@formatter:on				
		taskletStep.setAllowStartIfComplete(true);
		return taskletStep;

	}

	@Bean
	public Job job() {
		//@formatter:off
		Job job = jobBuilderFactory
													.get("job1234")
													.start(step())													
													.build();		
		//@formatter:on
		return job;

	}

}
