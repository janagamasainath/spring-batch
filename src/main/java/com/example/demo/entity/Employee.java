package com.example.demo.entity;


import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity (name = "EMPLOYEE_BATCH")
public class Employee {

	@Id
	private int id;
	private String name;
	private String designation;
	private int salary;
	private String deparment;
}
