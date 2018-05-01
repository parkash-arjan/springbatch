package com.fibonacci.springbatch.xml_files;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

//@Configuration
public class XMLJobConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public ItemWriter<Customer> itemWriter() {		
		return items -> {
			System.out.println("***********************************************");
			for (Customer item : items) {
				System.out.println(item);
			}
		};
	}

	@Bean
	public StaxEventItemReader<Customer> ItemReader() {
		System.out.println("=============================================");
		XStreamMarshaller unmarshaller = new XStreamMarshaller();

		Map<String, Class> aliases = new HashMap<>();
		aliases.put("customer", Customer.class);

		unmarshaller.setAliases(aliases);

		StaxEventItemReader<Customer> reader = new StaxEventItemReader<>();

		reader.setResource(new ClassPathResource("/customers.xml"));
		reader.setFragmentRootElementName("customer");
		reader.setUnmarshaller(unmarshaller);		
		return reader;
	}

	@Bean
	public Step step() {
		//@formatter:off
		TaskletStep tasklet = stepBuilderFactory
													.get("xml.job.config.step")
													.<Customer, Customer>chunk(5)
													.reader(ItemReader())
													.writer(itemWriter())
													.build();
		//@formatter:on
		tasklet.setAllowStartIfComplete(true);
		return tasklet;

	}

	@Bean
	public Job job() {
		//@formatter:off
		Job job = jobBuilderFactory
													.get("xml.job.config.job")
													.start(step())
													.build();
		//@formatter:on		
		return job;

	}

}
