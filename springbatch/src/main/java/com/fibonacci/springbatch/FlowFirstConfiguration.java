package com.fibonacci.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@EnableBatchProcessing
public class FlowFirstConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	public Step localStep() {

		return stepBuilderFactory.get("localStep").tasklet((stepContrbution, chunkContext) -> {
			System.out.println("FlowFirstConfiguration==> calling localStep()...");
			return RepeatStatus.FINISHED;
		}).build();

	}

	@Bean
	public Job flowFirstJob(Flow flow) {
		return jobBuilderFactory.get("flowFirstJob").start(flow).next(localStep()).end().build();
	}
}
