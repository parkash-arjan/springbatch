package com.fibonacci.springbatch;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class FlowConfiguration {

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Step step1() {

		// @formatter:off
		return stepBuilderFactory
					.get("step1")
					.tasklet((stepContibution, chunkContext) -> {
							System.out.println("FlowConfiguration ==> step#1");
							return RepeatStatus.FINISHED;
						})
					.build();
		// @formatter:on
	}

	@Bean
	public Step step2() {
		// @formatter:off
		return stepBuilderFactory
					.get("step2")
					.tasklet((stepContibution, chunkContext) -> {
							System.out.println("FlowConfiguration ==> step#2");
							return RepeatStatus.FINISHED;
						})
					.build();
		// @formatter:on
	}

	@Bean
	public Flow flow() {
		FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow");

		// @formatter:off
		flowBuilder.start(step1())
							.next(step2())
							.end();
		// @formatter:on

		return flowBuilder.build();
	}

}
