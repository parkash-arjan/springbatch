package com.fibonacci.springbatch.flat_files;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class JobConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public FlatFileItemReader<Student> flatFileItemReader() {		
		FlatFileItemReader<Student> fileItemReader = new FlatFileItemReader<>();

		fileItemReader.setLinesToSkip(1);
		fileItemReader.setResource(new ClassPathResource("/data.csv"));

		DefaultLineMapper<Student> defaultLineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setNames(new String[] { "id", "name", "email", "birthDate" });

		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
		defaultLineMapper.setFieldSetMapper(new CustomFieldMapper());
		defaultLineMapper.afterPropertiesSet();

		fileItemReader.setLineMapper(defaultLineMapper);

		return fileItemReader;
	}

	@Bean
	public ItemWriter<Student> itemWriter() {

		return items -> {
			System.out.println("********************************************************");
			for (Student student : items) {
				System.out.println(student);
			}

		};

	}

	@Bean
	public Step step() {				
		//@formatter:off
		return stepBuilderFactory.get("step")
													.<Student, Student>chunk(10)
													.reader(flatFileItemReader())
													.writer(itemWriter())
													.build();
		//@formatter:on
	}

	@Bean
	public Job job() {
		//@formatter:off
		return jobBuilderFactory.get("job")
												  .start(step())
												  .build();
		//@formatter:on		
	}
}
