package com.fibonacci.springbatch.database;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfiguration_PagingItemReader {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource dataSource;

	@Bean           
	public JdbcPagingItemReader<Customer> pagingItemReader() {
		//This is thread safe
		JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();

		reader.setDataSource(dataSource);
		reader.setFetchSize(10); // god to have same as chunk size
		reader.setRowMapper(new CustomerRowMapper());

		OraclePagingQueryProvider queryProvider = new OraclePagingQueryProvider();
		queryProvider.setSelectClause("ID, FIRSTNAME, LASTNAME");
		queryProvider.setFromClause("FROM CUSTOMER");

		Map<String, Order> sortKeys = new HashMap<>(1);

		sortKeys.put("ID", Order.ASCENDING);

		queryProvider.setSortKeys(sortKeys);

		reader.setQueryProvider(queryProvider);

		return reader;
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
													.get("steppagingItemReader")
													.<Customer, Customer>chunk(10)
													.reader(pagingItemReader())
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
													.get("jobpagingItemReader")
													.start(step())													
													.build();		
		//@formatter:on
		return job;

	}

}
