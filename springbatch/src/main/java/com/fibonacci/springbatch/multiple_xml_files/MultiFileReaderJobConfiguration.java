
package com.fibonacci.springbatch.multiple_xml_files;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

//@Configuration
public class MultiFileReaderJobConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Value("classpath*:/multiple_files/customer*.csv")
	private Resource[] inputFiles;

	@Bean
	public MultiResourceItemReader<Customer> multiResourceItemReader() {
		// will read files one by one
		MultiResourceItemReader<Customer> reader = new MultiResourceItemReader<>();

		reader.setDelegate(customerItemReader());
		reader.setResources(inputFiles);

		return reader;
	}

	@Bean
	public FlatFileItemReader<Customer> customerItemReader() {
		FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();

		DefaultLineMapper<Customer> customerLineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] { "id", "firstName", "lastName", "birthdate" });

		customerLineMapper.setLineTokenizer(tokenizer);
		customerLineMapper.setFieldSetMapper(new CustomerFieldSetMapper());
		customerLineMapper.afterPropertiesSet();

		reader.setLineMapper(customerLineMapper);

		return reader;
	}

	@Bean
	public ItemWriter<Customer> ItemWriter() {
		return items -> {
			System.out.println("************************");
			for (Customer item : items) {
				System.out.println(item);
			}
		};
	}

	@Bean
	public Step step() {
		//@formatter:off
		TaskletStep tasklet = stepBuilderFactory
																.get("multi.file.reader.job.configuration.step")
																.<Customer, Customer>chunk(10)
																.reader(multiResourceItemReader())
																.writer(ItemWriter())
																.build();
		//@formatter:on
		tasklet.setAllowStartIfComplete(true);
		return tasklet;
	}

	@Bean
	public Job job() {
		//@formatter:off
		Job job = jobBuilderFactory
										.get("multi.file.reader.job.configuration.job")
										.start(step())
										.build();
		//@formatter:on

		return job;
	}
}
