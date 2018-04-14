package com.fibonacci.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class StepTransitionConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Step step1() {

		//@formatter:off
		return stepBuilderFactory.
				get("step1").
				tasklet(new Tasklet() {
					@Override
					public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
						System.out.println("StepTransitionConfiguration ==> Step#1");
						return RepeatStatus.FINISHED;
					}
		}).build();
		//@formatter:on
	}

	@Bean
	public Step step2() {

		//@formatter:off
		return stepBuilderFactory.
				get("step2").
				tasklet(new Tasklet() {
					@Override
					public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
						System.out.println("StepTransitionConfiguration ==> Step#2");
						return RepeatStatus.FINISHED;
					}
		}).build();
		//@formatter:on
	}

	@Bean
	public Step step3() {
		//@formatter:off		
		return stepBuilderFactory.
				get("step3").
				tasklet((stepContribution,chunkContext)->{
					System.out.println("StepTransitionConfiguration ==> Step#3");
					return RepeatStatus.FINISHED;
				}).
				build();
		//@formatter:on		
	}

	@Bean
	public Job transitionJobSimpleNext() {
		//@formatter:off
		//return jobBuilderFactory.get("transitionJobSimpleNext").					
					//start(step1()).
					//next(step2()).
					//next(step3()).
					//next(step2()). we can repeat the step if wanted
					//build();
		//@formatter:on

		//@formatter:off
		return jobBuilderFactory.get("transitionJobSimpleNext").					
					start(step1()).on("COMPLETED").to(step2()).
					from(step2()).on("COMPLETED").to(step3()).
					from(step3()).end().
					build();
		//@formatter:on				

	}
}
