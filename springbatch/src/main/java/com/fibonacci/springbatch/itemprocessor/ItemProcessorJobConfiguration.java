package com.fibonacci.springbatch.itemprocessor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

@Configuration
public class ItemProcessorJobConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;

	@Bean
	public JdbcPagingItemReader<Customer> pagingItemReader() {
		JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();

		reader.setDataSource(this.dataSource);
		reader.setFetchSize(10);
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
	public StaxEventItemWriter<Customer> customerItemWriter() throws Exception {

		XStreamMarshaller marshaller = new XStreamMarshaller();

		Map<String, Class> aliases = new HashMap<>();
		aliases.put("customer", Customer.class);

		marshaller.setAliases(aliases);

		StaxEventItemWriter<Customer> itemWriter = new StaxEventItemWriter<>();

		itemWriter.setRootTagName("customers");
		itemWriter.setMarshaller(marshaller);
		String customerOutputPath = File.createTempFile("customerOutput", ".xml").getAbsolutePath();
		System.out.println(">> Output Path: " + customerOutputPath);
		itemWriter.setResource(new FileSystemResource(customerOutputPath));

		itemWriter.afterPropertiesSet();

		return itemWriter;
	}

	@Bean
	public UpperCaseItemProcessor itemProcessor() {
		return new UpperCaseItemProcessor();
	}

	@Bean
	public Step step1() throws Exception {
		return stepBuilderFactory.get("Item.Processor.Job.Configuration.Step").<Customer, Customer>chunk(10).reader(pagingItemReader()).processor(itemProcessor()).writer(customerItemWriter()).build();
	}

	@Bean
	public Job job() throws Exception {
		return jobBuilderFactory.get("Item.Processor.Job.Configuration.Job").start(step1()).build();
	}
}
