package com.fibonacci.springbatch.nestedjob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

//@Configuration
public class ParentJobConfiguration {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private Job childJob;

	@Autowired
	private JobLauncher jobLauncher;

	@Bean
	public Step step1() {
		Tasklet tasklet = new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
				System.out.println("ParentJobConfiguration = >> step1()");
				return RepeatStatus.FINISHED;
			}
		};
		TaskletStep taskletStep = stepBuilderFactory.get("step1").tasklet(tasklet).build();
		return taskletStep;

	}

	@Bean
	public Job parentJob(JobRepository JobRepository, PlatformTransactionManager platformTransactionManager) {

		StepBuilder StepBuilder = new StepBuilder("childJobStep");
		//@formatter:off		
		Step childJobStep = new JobStepBuilder(StepBuilder).
												   job(childJob).
												   launcher(jobLauncher).
												   repository(JobRepository).
												   transactionManager(platformTransactionManager).
												   build();
		//@formatter:on		
		Job job = jobBuilderFactory.get("parentJob").start(step1()).next(childJobStep).build();
		return job;
	}
}