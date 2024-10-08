package com.example.demo.configuration;

import org.springframework.batch.item.ItemProcessor;

import com.example.demo.entity.Employee;

public class EmployeeProcessing implements ItemProcessor<Employee, Employee>{

	@Override
	public Employee process(Employee item) throws Exception {
		// TODO Auto-generated method stub
		//logic to process
		return null;
	}

}
