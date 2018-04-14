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

@Configuration
@EnableBatchProcessing
public class FlowLastConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	public Step localStep() {

		return stepBuilderFactory.get("localStep").tasklet((stepContrbution, chunkContext) -> {
			System.out.println("FlowLastConfiguration==> calling localStep()...");
			return RepeatStatus.FINISHED;
		}).build();

	}

	@Bean
	public Job flowLasttJob(Flow flow) {
		return jobBuilderFactory.get("flowLasttJob").start(localStep()).on("COMPLETED").to(flow).end().build();
	}
}
