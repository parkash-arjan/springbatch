package com.fibonacci.springbatch.split;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

//@Configuration
public class SplitBatchConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Flow flow1() {

		Step step1 = stepBuilderFactory.get("step1").tasklet(tasklet()).build();

		FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow");
		Flow flow = flowBuilder.start(step1).build();

		return flow;
	}

	@Bean
	public Flow flow2() {

		Step step2 = stepBuilderFactory.get("step2").tasklet(tasklet()).build();
		Step step3 = stepBuilderFactory.get("step3").tasklet(tasklet()).build();

		FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow");
		Flow flow = flowBuilder.start(step2).next(step3).build();

		return flow;
	}

	@Bean
	public Job job() {

		Job job = jobBuilderFactory.get("job").start(flow1()).split(new SimpleAsyncTaskExecutor()).add(flow2()).end().build();
		return job;
	}

	@Bean
	public Tasklet tasklet() {
		return new CountingTasklet();
	}

	public static class CountingTasklet implements Tasklet {
		@Override
		public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
			System.out.println(String.format("%s has been executed on thread %s", chunkContext.getStepContext().getStepName(), Thread.currentThread().getName()));
			return RepeatStatus.FINISHED;
		}

	}

}
