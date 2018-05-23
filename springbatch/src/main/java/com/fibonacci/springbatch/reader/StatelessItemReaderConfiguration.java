package com.fibonacci.springbatch.reader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class StatelessItemReaderConfiguration {

	@Autowired
	private JobBuilderFactory jobbuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public StatelessItemReader statelessItemReader() {
		List<String> data = new ArrayList<>();
		for (int i = 1; i <= 10; i++) {
			data.add("Item#" + i);
		}

		return new StatelessItemReader(data);
	}

	@Bean
	public Step stepA() {
		//@formatter:off
		 TaskletStep tasklet = stepBuilderFactory.
					get("stepA")
					.<String,String>chunk(0) //two transactions for the data???
					.reader(statelessItemReader())
					.writer(list->{
						for (String string : list) {
							System.out.println(String.format("Data Read %s", string));
						}
					})				
					.build();		 		 
		//@formatter:on	
		tasklet.setAllowStartIfComplete(true);
		return tasklet;
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
