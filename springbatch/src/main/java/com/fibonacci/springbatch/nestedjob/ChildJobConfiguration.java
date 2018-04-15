//package com.fibonacci.springbatch.nestedjob;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.StepContribution;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.scope.context.ChunkContext;
//import org.springframework.batch.core.step.tasklet.Tasklet;
//import org.springframework.batch.core.step.tasklet.TaskletStep;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
////@Configuration
//public class ChildJobConfiguration {
//
//	@Autowired
//	private JobBuilderFactory jobBuilderFactory;
//
//	@Autowired
//	private StepBuilderFactory stepBuilderFactory;
//
//	@Bean
//	public Step step1a() {
//		Tasklet tasklet = new Tasklet() {
//			@Override
//			public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
//				System.out.println("ChildJobConfiguration = >> step1a()");
//				return RepeatStatus.FINISHED;
//			}
//		};
//		TaskletStep taskletStep = stepBuilderFactory.get("step1a").tasklet(tasklet).build();
//		return taskletStep;
//
//	}
//
//	@Bean
//	public Job childJob() {
//		Job job = jobBuilderFactory.get("childJob").start(step1a()).build();
//		return job;
//	}
//}
