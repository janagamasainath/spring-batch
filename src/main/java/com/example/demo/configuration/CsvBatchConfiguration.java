package com.example.demo.configuration;

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

import com.example.demo.entity.Employee;
import com.example.demo.repo.EmployeeRepository;

@Configuration
@EnableBatchProcessing
public class CsvBatchConfiguration {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	//create reader
	@Bean
	public FlatFileItemReader<Employee> employeeReader(){
		FlatFileItemReader<Employee> filereader= new FlatFileItemReader<>();
		filereader.setResource(new FileSystemResource( "src/main/resources/employee.csv"));
		filereader.setName("csv-Reader");
		filereader.setLinesToSkip(1);
		filereader.setLineMapper(lineMapper());
		return filereader;
	}

	private LineMapper<Employee> lineMapper() {
		// TODO Auto-generated method stub
		DefaultLineMapper<Employee> linemapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer token= new DelimitedLineTokenizer();
		token.setDelimiter(",");
		token.setStrict(false);
		token.setNames("id","name","designation","salary","deparment");
		BeanWrapperFieldSetMapper<Employee> fieldSetMapper= new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Employee.class);
		linemapper.setLineTokenizer(token);
		linemapper.setFieldSetMapper(fieldSetMapper);
		return linemapper;
	}
	
	//process the file
	
	@Bean
	public EmployeeProcessing employeeProcessing() {
		return new EmployeeProcessing();
	}
	
	
	//create Writer
	
	@Bean
	public RepositoryItemWriter<Employee> employeeItemWriter(){
		RepositoryItemWriter<Employee> repowriter= new RepositoryItemWriter<>();
		repowriter.setRepository(employeeRepository);
		repowriter.setMethodName("save");
		return repowriter;
	}
	
	//create step
	
	public Step step1() {
		return stepBuilderFactory.get("Step-1").<Employee, Employee>chunk(10)
				.reader(employeeReader())
				.processor(employeeProcessing())
				.writer(employeeItemWriter()).build();
	}
	
	//create job
	
	public Job job() {
		return jobBuilderFactory.get("Employee-job")
				.flow(step1())
				.end()
				.build();
	}
	
	
	 

}
