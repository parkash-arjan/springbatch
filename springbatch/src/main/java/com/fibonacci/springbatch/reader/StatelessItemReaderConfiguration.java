package com.fibonacci.springbatch.reader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatelessItemReaderConfiguration {

	@Autowired
	private JobBuilderFactory jobbuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public StatelessItemReader statelessItemReader() {
		List<String> data = new ArrayList<>(2);
		data.add("Item#1");
		data.add("Item#2");
		data.add("Item#3");

		return new StatelessItemReader(data);
	}

	@Bean
	public Step stepA() {
		//@formatter:off
		return stepBuilderFactory.
					get("stepA")
					.<String,String>chunk(2) //two transactions for the data???
					.reader(statelessItemReader())
					.writer(list->{
						for (String string : list) {
							System.out.println(String.format("Data Read %s", string));
						}
					})
					.build();
		//@formatter:on					
	}

	@Bean
	public Job statelessItemReadeJob() {
		//@formatter:off
		return jobbuilderFactory
					.get("statelessItemReadeJob")
					.start(stepA())
					.build();
		//@formatter:on
	}

}
