package com.fibonacci.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@EnableBatchProcessing
public class DeciderBatchConfiguration {

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Bean
	public Step startStep() {

		Tasklet tasklet = new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
				System.out.println("DeciderBatchConfiguration::startStep()");
				return RepeatStatus.FINISHED;
			}
		};

		TaskletStep step = stepBuilderFactory.get("startStep").tasklet(tasklet).build();
		return step;

	}

	@Bean
	public Step evenStep() {

		Tasklet tasklet = new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
				System.out.println("DeciderBatchConfiguration::evenStep()");
				return RepeatStatus.FINISHED;
			}
		};

		TaskletStep step = stepBuilderFactory.get("evenStep").tasklet(tasklet).build();
		return step;

	}

	@Bean
	public Step oddStep() {

		Tasklet tasklet = new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution oddStep, ChunkContext chunkContext) throws Exception {
				System.out.println("DeciderBatchConfiguration::oddStep()");
				return RepeatStatus.FINISHED;
			}
		};

		TaskletStep step = stepBuilderFactory.get("oddStep").tasklet(tasklet).build();
		return step;

	}

	@Bean
	public JobExecutionDecider decider() {
		return new EvenOddDecider();
	}

	@Bean
	public Job job() {
		//@formatter:off
		//state of decider are persisted in job repository.
		Job job = jobBuilderFactory.get("JOB").
										start(startStep()).
										next(decider()).
										from(decider()).on("ODD").to(oddStep()).
										from(decider()).on("EVEN").to(evenStep()).
										from(oddStep()).on("*").to(decider()).
										//from(evenStep()).on("*").to(decider()).
										end().
										build();
		//@formatter:on		
		return job;
	}

	public static class EvenOddDecider implements JobExecutionDecider {

		private int count = 0;

		@Override
		public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution lastStepExecuted) {
			count++;

			if (count % 2 == 0) {
				return new FlowExecutionStatus("EVEN");
			} else {
				return new FlowExecutionStatus("ODD");
			}

		}

	}
}
