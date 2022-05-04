package com.aktechie.base.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.aktechie.base.entity.Customer;
import com.aktechie.base.repository.CustomerRepository;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	private CustomerRepository customerRepository;
	
	@Bean
	public FlatFileItemReader<Customer> reader(){
		FlatFileItemReader<Customer> fileItemReader=new FlatFileItemReader<>();
		fileItemReader.setResource(new FileSystemResource("src//main//resources//customer.csv"));
		fileItemReader.setName("itemReader");
		fileItemReader.setLinesToSkip(1);
		fileItemReader.setLineMapper(lineMapper());
		fileItemReader.setStrict(false);
		return fileItemReader;
	}
	@Bean
	public LineMapper<Customer> lineMapper(){
		DefaultLineMapper<Customer> lineMapper=new DefaultLineMapper<>();
		DelimitedLineTokenizer delimitedLineTokenizer=new DelimitedLineTokenizer();
		delimitedLineTokenizer.setDelimiter(",");
		delimitedLineTokenizer.setStrict(false);
		delimitedLineTokenizer.setNames("id","firstName","lastname","email","gender","contactNo","country","dob");
		BeanWrapperFieldSetMapper<Customer> fieldMapper=new BeanWrapperFieldSetMapper<Customer>();
		fieldMapper.setTargetType(Customer.class);
		lineMapper.setFieldSetMapper(fieldMapper);
		lineMapper.setLineTokenizer(delimitedLineTokenizer);;
		return lineMapper;
	}
	@Bean
	public CustomerProcessor processor() {
		return new CustomerProcessor();
	}
	@Bean
	public RepositoryItemWriter<Customer> writer(){
		RepositoryItemWriter<Customer> writer=new RepositoryItemWriter<>();
		writer.setRepository(customerRepository);
		writer.setMethodName("save");
		return writer;
	}
	@Bean
	public Step step1() {
		
		return stepBuilderFactory.get("csv-step").<Customer,Customer>chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.taskExecutor(asyncTaskExecutor())
				.build();
				
	}
	@Bean
	public Job runJob() {
		return jobBuilderFactory.get("importcustomer")
				.flow(step1())
				.end().build();
	}
	
	@Bean
	public TaskExecutor asyncTaskExecutor() {
		SimpleAsyncTaskExecutor async=new SimpleAsyncTaskExecutor();
		async.setConcurrencyLimit(5);
		return async;
	}
}
